package com.intergamma.inventory.service.exception;

public class ProductNotAvailableAtStoreException extends InventoryException {
    public ProductNotAvailableAtStoreException(String storeName, String productCode) {
        super("product " + productCode + " is not available at store " + storeName);
    }
}
