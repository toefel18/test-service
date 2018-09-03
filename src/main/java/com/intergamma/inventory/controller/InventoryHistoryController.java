package com.intergamma.inventory.controller;

import com.intergamma.inventory.dto.InventoryQueryResult;
import com.intergamma.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class InventoryHistoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/inventory/store/{storeName}/{productCode}")
    public InventoryQueryResult inventoryForProduct(@PathVariable String storeName, @PathVariable String productCode) {
        return inventoryService.calculateInventory(storeName, productCode);
    }
//
//    @PostMapping("/inventory/store/{storeName}/{productCode}")
//    public void addNewInventory(@PathVariable String storeName, @PathVariable String productCode) {
//        return inventoryService.calculateInventory(storeName, productCode);
//    }
//
//    @DeleteMapping("/inventory/store/{storeName}/{productCode}")
//    public void addNewInventory(@PathVariable String storeName, @PathVariable String productCode) {
//        return inventoryService.setInventoryTo(storeName, productCode);
//    }

}
