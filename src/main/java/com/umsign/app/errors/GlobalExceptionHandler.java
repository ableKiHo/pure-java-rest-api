package com.umsign.app.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.umsign.app.api.Constants;
import com.umsign.app.api.ErrorResponse;

import java.io.IOException;
import java.io.OutputStream;

public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handle(Throwable throwable, HttpExchange exchange) {
        try {
            throwable.printStackTrace();
            exchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            ErrorResponse response = getErrorResponse(throwable, exchange);
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(objectMapper.writeValueAsBytes(response));
            responseBody.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ErrorResponse getErrorResponse(Throwable throwable, HttpExchange exchange) throws IOException {
        ErrorResponse.ErrorResponseBuilder responseBuilder = ErrorResponse.builder();
        if(throwable instanceof InvalidRequestException) {
            InvalidRequestException exc = (InvalidRequestException) throwable;
            responseBuilder.message(exc.getMessage()).code(exc.getCode());
            exchange.sendResponseHeaders(400,0);
        }
        return responseBuilder.build();
    }
}
