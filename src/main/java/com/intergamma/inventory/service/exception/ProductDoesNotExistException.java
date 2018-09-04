package com.intergamma.inventory.service.exception;

public class ProductDoesNotExistException extends InventoryException {
    public ProductDoesNotExistException(String productCode) {
        super("no product with code " + productCode + " exists");
    }
}
