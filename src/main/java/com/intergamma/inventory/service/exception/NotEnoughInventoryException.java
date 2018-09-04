package com.intergamma.inventory.service.exception;

public class NotEnoughInventoryException extends InventoryException {
    public NotEnoughInventoryException(String storeName, String productCode, Long actual, Long available) {
        super("product " + productCode + " has not enough inventory at store " + storeName +
                ", actual inventory is " + actual + " available is " + available);
    }
}
