package com.shopdb.ecocitycraft.shopdb.models.exceptions;

public class AlreadyExistentException extends RuntimeException {
    public AlreadyExistentException(String message) {
        super(message);
    }
}
