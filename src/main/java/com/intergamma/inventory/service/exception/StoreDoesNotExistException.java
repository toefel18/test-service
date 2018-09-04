package com.intergamma.inventory.service.exception;

public class StoreDoesNotExistException extends InventoryException {
    public StoreDoesNotExistException(String storeName) {
        super("no store with name " + storeName + " exists");
    }
}
