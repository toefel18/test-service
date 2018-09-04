package com.intergamma.inventory.service;

import com.intergamma.inventory.access.InventoryHistoryRepository;
import com.intergamma.inventory.access.ProductRepository;
import com.intergamma.inventory.access.ReservationRepository;
import com.intergamma.inventory.access.StoreRepository;
import com.intergamma.inventory.dto.InventoryQueryResultDto;
import com.intergamma.inventory.entity.*;
import com.intergamma.inventory.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class InventoryService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryHistoryRepository inventoryHistoryRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Calculates the inventory of a product at a given store, returns Optional.empty() when the store does not carry
     * the product.
     */
    public InventoryQueryResultDto calculateInventory(String storeName, String productCode) {
        assertStoreAndProductCodeExists(storeName, productCode);

        Optional<Long> actualInventory = calculateActualInventory(storeName, productCode);

        if (actualInventory.isPresent()) {
            Optional<Long> reservedInventory = reservationRepository.calculateTotalAmountReserved(storeName, productCode);
            return InventoryQueryResultDto.createFor(storeName, productCode, actualInventory.get(), reservedInventory.orElse(0L));
        } else {
            throw new ProductNotAvailableAtStoreException(storeName, productCode);
        }
    }

    private void assertStoreAndProductCodeExists(String storeName, String productCode) {
        Optional<Store> store = storeRepository.findByName(storeName);
        Optional<Product> product = productRepository.findByProductCode(productCode);
        assertStoreAndProductExist(store, storeName, product, productCode);
    }

    private Optional<Long> calculateActualInventory(String storeName, String productCode) {
        Optional<Long> addedToInventory = inventoryHistoryRepository.calculateTotalAmountByType(storeName, productCode, InventoryHistoryType.ADD);
        Optional<Long> removedFromInventory = inventoryHistoryRepository.calculateTotalAmountByType(storeName, productCode, InventoryHistoryType.REMOVE);

        return addedToInventory.map(addedInventory -> addedInventory - removedFromInventory.orElse(0L));
    }

    public void setInventory(String storeName, String productCode, String clientName, long amount) {
        if (amount < 0) {
            throw new InvalidAmountException(amount);
        }

        Optional<Store> store = storeRepository.findByName(storeName);
        Optional<Product> product = productRepository.findByProductCode(productCode);
        assertStoreAndProductExist(store, storeName, product, productCode);

        Optional<Long> actualInventory = calculateActualInventory(storeName, productCode);

        if (!actualInventory.isPresent()) {
            addTransaction(new InventoryHistory(store.get(), product.get(), clientName, InventoryHistoryType.ADD, amount));
        } else {
            long delta = amount - actualInventory.get();
            if (delta < 0) {
                addTransaction(new InventoryHistory(store.get(), product.get(), clientName, InventoryHistoryType.REMOVE, Math.abs(delta)));
            } else if (delta > 0) {
                addTransaction(new InventoryHistory(store.get(), product.get(), clientName, InventoryHistoryType.ADD, delta));
            } else {
                LOG.info("no transaction required to reach inventory with amount={}", amount);
            }
        }
    }

    private void assertStoreAndProductExist(Optional<Store> store, String storeName, Optional<Product> product, String productCode) {
        store.orElseThrow(() -> new StoreDoesNotExistException(storeName));
        product.orElseThrow(() -> new ProductDoesNotExistException(productCode));
    }

    private void addTransaction(InventoryHistory transaction) {
        LOG.info("Recording inventory change storeName={} productCode={} type={} amount={}",
                transaction.getStore().getName(), transaction.getProduct().getProductCode(),
                transaction.getTransactionType().name(), transaction.getAmount());
        inventoryHistoryRepository.save(transaction);
    }

    public void clearInventory(String storeName, String productCode) {
        assertStoreAndProductCodeExists(storeName, productCode);
        setInventory(storeName, productCode, "system", 0);
        //TODO clear reservations or block if reservations present?
    }

    public void reserveInventory(String storeName, String productCode, String clientName, long amount) {
        InventoryQueryResultDto inventory = calculateInventory(storeName, productCode);

        if (inventory.inventory == null) {
            throw new ProductNotAvailableAtStoreException(storeName, productCode);
        }

        Optional<Reservation> existingReservation = reservationRepository.findByStore_NameAndProduct_ProductCodeAndClientName(storeName, productCode, clientName);

        if (existingReservation.isPresent()) {
            updateExistingReservation(inventory, existingReservation.get(), amount);
        } else {
            saveNewReservation(inventory, clientName, amount);
        }
    }

    private void updateExistingReservation(InventoryQueryResultDto inventory, Reservation existingReservation, long newAmount) {
        LOG.info("Updating existing inventory reservation for storeName={} productCode={} clientName={} fromAmount={} toAmount={}",
                inventory.storeName, inventory.productCode, existingReservation.getClientName(), existingReservation.getAmount(), newAmount);
        long availableWithoutExistingReservation = inventory.inventory - inventory.reserved + existingReservation.getAmount();

        if (availableWithoutExistingReservation - newAmount < 0) {
            LOG.warn("Failed to update existing inventory reservation, not enough inventory available to cover new amount" +
                            " for storeName={} productCode={} clientName={} desiredAmount={} availableAmount={}",
                    inventory.storeName, inventory.productCode, existingReservation.getClientName(), newAmount, availableWithoutExistingReservation);

            throw new NotEnoughInventoryException(inventory.storeName, inventory.productCode, inventory.inventory, inventory.inventory - inventory.reserved);
        }

        existingReservation.setAmount(newAmount);
    }

    private void saveNewReservation(InventoryQueryResultDto inventory, String clientName, long amount) {
        LOG.info("Creating new inventory reservation for storeName={} productCode={} clientName={} amount={}",
                inventory.storeName, inventory.productCode, clientName, amount);

        Optional<Store> store = storeRepository.findByName(inventory.storeName);
        Optional<Product> product = productRepository.findByProductCode(inventory.productCode);
        assertStoreAndProductExist(store, inventory.storeName, product, inventory.productCode);

        long availableInventory = inventory.inventory - inventory.reserved;

        if (availableInventory < amount) {
            throw new NotEnoughInventoryException(inventory.storeName, inventory.productCode, inventory.inventory, availableInventory);
        }

        Reservation newReservation = new Reservation(store.get(), product.get(), clientName, amount);

        reservationRepository.save(newReservation);
    }

    public void releaseReservedInventory(String storeName, String productCode, String clientName) {
        LOG.info("Deleting inventory reservations for storeName={} productCode={} clientName={}", storeName, productCode, clientName);

        assertStoreAndProductCodeExists(storeName, productCode);

        reservationRepository.deleteByStore_NameAndProduct_ProductCodeAndClientName(storeName, productCode, clientName);
    }
}
