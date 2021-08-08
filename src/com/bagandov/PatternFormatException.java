package com.bagandov;

public class PatternFormatException extends RuntimeException {

    public PatternFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatternFormatException(String message) {
        super(message);
    }
}
