package com.jonathan.reggie.common;

/**
 * Custom business exception
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
