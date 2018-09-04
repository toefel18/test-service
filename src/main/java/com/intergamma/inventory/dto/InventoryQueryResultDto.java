package com.intergamma.inventory.dto;

public class InventoryQueryResultDto {
    public static InventoryQueryResultDto createFor(String storeName, String productCode, long inventory, long reserved) {
        InventoryQueryResultDto result = new InventoryQueryResultDto();
        result.storeName = storeName;
        result.productCode = productCode;
        result.inventory = inventory;
        result.reserved = reserved;
        return result;
    }

    public String storeName;
    public String productCode;

    public Long inventory = null;
    public Long reserved = null;
}
