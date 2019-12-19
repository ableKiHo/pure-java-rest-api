package com.umsign.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.umsign.app.api.user.PasswordEncoder;
import com.umsign.app.api.user.RegistrationRequest;
import com.umsign.app.api.user.RegistrationResponse;
import com.umsign.app.errors.ApplicationExceptions;
import com.umsign.app.errors.GlobalExceptionHandler;
import com.umsign.app.errors.InvalidRequestException;
import com.umsign.domain.user.NewUser;
import com.umsign.domain.user.UserService;
import io.vavr.control.Try;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public abstract class Handler {

    private final ObjectMapper objectMapper;
    private final GlobalExceptionHandler exceptionHandler;

    public Handler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
        this.objectMapper = objectMapper;
        this.exceptionHandler = exceptionHandler;
    }

    public void handle(HttpExchange exchange) {
        Try.run(() -> excute(exchange))
                .onFailure(thr -> exceptionHandler.handle(thr, exchange));
    }

    protected abstract void excute(HttpExchange exchange) throws Exception;


    protected <T> T readPostRequest(InputStream is, Class<T> type){
        return Try.of(() -> objectMapper.readValue(is, type))
                .getOrElseThrow(ApplicationExceptions.invalidRequest());
    }

    protected <T> T readGetRequest(Map map, Class<T> type){
        return Try.of(() -> objectMapper.convertValue(map, type))
                .getOrElseThrow(ApplicationExceptions.invalidRequest());
    }

    protected <T> byte[] writeResponse(T response) {
        return Try.of(() -> objectMapper.writeValueAsBytes(response))
                .getOrElseThrow(ApplicationExceptions.invalidRequest());
    }

    protected  static Headers getHeaders(String key, String value) {
        Headers headers = new Headers();
        headers.set(key, value);
        return headers;
    }


}
