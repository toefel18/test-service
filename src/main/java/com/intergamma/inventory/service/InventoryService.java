package com.intergamma.inventory.service;

import com.intergamma.inventory.access.InventoryHistoryRepository;
import com.intergamma.inventory.access.ProductRepository;
import com.intergamma.inventory.access.StoreRepository;
import com.intergamma.inventory.dto.InventoryQueryResult;
import com.intergamma.inventory.entity.InventoryHistoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

@Service
public class InventoryService {

    private static final Duration MAX_TIME_RESERVATION = Duration.ofMinutes(30);

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryHistoryRepository inventoryHistoryRepository;
    /**
     * Calculates the inventory of a product at a given store, returns Optional.empty() when the store does not carry
     * the product.
     */
    public InventoryQueryResult calculateInventory(String storeName, String productCode) {
        Optional<Long> addedToInventory = inventoryHistoryRepository.calculateTotalAmountByType(storeName, productCode, InventoryHistoryType.ADD);
        Optional<Long> removedFromInventory = inventoryHistoryRepository.calculateTotalAmountByType(storeName, productCode, InventoryHistoryType.REMOVE);
        Optional<Long> actualInventory = addedToInventory.map(addedInventory -> addedInventory - removedFromInventory.orElse(0L));

        if (actualInventory.isPresent()) {
            Optional<BigDecimal> reservedInventory = inventoryHistoryRepository.calculateReserved(storeName, productCode, MAX_TIME_RESERVATION.getSeconds());
            return InventoryQueryResult.createResult(storeName, productCode, actualInventory.get(), reservedInventory.map(BigDecimal::longValue)
                    .orElse(0L));
        } else {
            return InventoryQueryResult.createNoInventoryFoundResult(storeName, productCode);
        }
    }

}
