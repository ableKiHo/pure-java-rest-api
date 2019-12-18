package com.umsign.app.errors;

public class MethodNotAllowedException extends ApplicationException {
    public MethodNotAllowedException(int code, String message) {
        super(code, message);
    }
}
