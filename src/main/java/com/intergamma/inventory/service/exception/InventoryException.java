package com.intergamma.inventory.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class InventoryException extends RuntimeException {
    public InventoryException() {
    }

    public InventoryException(String s) {
        super(s);
    }

    public InventoryException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InventoryException(Throwable throwable) {
        super(throwable);
    }

    public InventoryException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
