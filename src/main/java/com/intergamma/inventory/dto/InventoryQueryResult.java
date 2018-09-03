package com.intergamma.inventory.dto;

public class InventoryQueryResult {
    public static InventoryQueryResult createNoInventoryFoundResult(String storeName, String productCode) {
        InventoryQueryResult noInventoryResult = new InventoryQueryResult();
        noInventoryResult.storeName = storeName;
        noInventoryResult.productCode = productCode;
        noInventoryResult.message = "store does not carry the product!";
        return noInventoryResult;
    }

    public static InventoryQueryResult createResult(String storeName, String productCode, long inventory, long reserved) {
        InventoryQueryResult result = new InventoryQueryResult();
        result.storeName = storeName;
        result.productCode = productCode;
        result.inventory = inventory;
        result.reserved = reserved;
        result.availableInventory = inventory - reserved;
        return result;
    }

    public String storeName;
    public String productCode;
    public String message = null;

    public Long inventory = null;
    public Long reserved = null;
    public Long availableInventory = null;
}
