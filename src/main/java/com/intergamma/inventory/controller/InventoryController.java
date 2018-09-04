package com.intergamma.inventory.controller;

import com.intergamma.inventory.dto.InventoryQueryResultDto;
import com.intergamma.inventory.dto.InventoryAmountDto;
import com.intergamma.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/inventories/stores/{storeName}/products/{productCode}")
    public InventoryQueryResultDto getInventoryForProduct(@PathVariable String storeName,
                                                          @PathVariable String productCode) {
        return inventoryService.calculateInventory(storeName, productCode);
    }

    @PutMapping("/inventories/stores/{storeName}/products/{productCode}")
    public void setInventoryForProduct(@PathVariable String storeName,
                                       @PathVariable String productCode,
                                       @RequestBody InventoryAmountDto amountDto) {
        inventoryService.setInventory(storeName, productCode, "system", amountDto.amount);
    }

    @DeleteMapping("/inventories/stores/{storeName}/products/{productCode}")
    public void clearInventoryForProduct(@PathVariable String storeName,
                                         @PathVariable String productCode) {
        inventoryService.clearInventory(storeName, productCode);
    }


    @PutMapping("/inventories/stores/{storeName}/products/{productCode}/reservations/{clientName}")
    public void reserveProductInventory(@PathVariable String storeName,
                                        @PathVariable String productCode,
                                        @PathVariable String clientName,
                                        @RequestBody InventoryAmountDto reservationAmount) {
        inventoryService.reserveInventory(storeName, productCode, clientName, reservationAmount.amount);
    }

    @DeleteMapping("/inventories/store/{storeName}/product/{productCode}/reservations/{clientName}")
    public void releaseProductInventory(@PathVariable String storeName,
                                        @PathVariable String productCode,
                                        @PathVariable String clientName) {
        inventoryService.releaseReservedInventory(storeName, productCode, clientName);
    }

}
