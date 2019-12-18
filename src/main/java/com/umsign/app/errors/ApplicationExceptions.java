package com.umsign.app.errors;

import java.util.function.Function;

public class ApplicationExceptions {
    public static Function<? super Throwable, RuntimeException> invalidRequest() {
        return thr -> new InvalidRequestException(400, thr.getMessage());
    }
}
