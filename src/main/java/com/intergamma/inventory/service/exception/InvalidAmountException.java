package com.intergamma.inventory.service.exception;

public class InvalidAmountException extends InventoryException {
    public InvalidAmountException(long amount) {
        super("invalid amount, must be positive or 0 but was: " + amount);
    }
}
