package com.umsign.app.errors;

public class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(int code, String message) {
        super(code, message);
    }
}
