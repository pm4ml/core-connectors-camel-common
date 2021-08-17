package com.modusbox.client.customexception;

public class InvalidAccountNumberException extends Exception {

    public InvalidAccountNumberException(String message) {
        super(message);
    }
}
