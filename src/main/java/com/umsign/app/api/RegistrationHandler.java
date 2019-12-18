package com.umsign.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.umsign.app.api.user.PasswordEncoder;
import com.umsign.app.api.user.RegistrationRequest;
import com.umsign.app.api.user.RegistrationResponse;
import com.umsign.app.errors.ApplicationException;
import com.umsign.app.errors.ApplicationExceptions;
import com.umsign.app.errors.GlobalExceptionHandler;
import com.umsign.app.errors.InvalidRequestException;
import com.umsign.domain.user.NewUser;
import com.umsign.domain.user.UserService;
import io.vavr.control.Try;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RegistrationHandler {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final GlobalExceptionHandler exceptionHandler;

    public RegistrationHandler(UserService userService, ObjectMapper objectMapper,  GlobalExceptionHandler exceptionHandler) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.exceptionHandler = exceptionHandler;
    }

    public void handle(HttpExchange exchange) throws IOException {
        if(!exchange.getRequestMethod().equals("POST")) {
            exceptionHandler.handle(new UnsupportedOperationException(),exchange);
        }
        RegistrationRequest registrationRequest = null;
        try {
            registrationRequest = readRequest(exchange.getRequestBody(), RegistrationRequest.class);
        }catch (Exception e) {
            exceptionHandler.handle(new InvalidRequestException(400, e.getMessage()),exchange);
        }

        NewUser user = NewUser.builder()
                .login(registrationRequest.getLogin())
                .password(PasswordEncoder.encode(registrationRequest.getPassword()))
                .build();

        String userId = userService.create(user);

        exchange.getResponseHeaders().set(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
        exchange.sendResponseHeaders(StatusCode.CREATED.getCode(), 0);

        byte[] response = writeResponse(new RegistrationResponse(userId));

        OutputStream responseBody = exchange.getResponseBody();
        responseBody.write(response);
        responseBody.close();

    }

    protected <T> T readRequest(InputStream is, Class<T> type){
        return Try.of(() -> objectMapper.readValue(is, type))
                .getOrElseThrow(ApplicationExceptions.invalidRequest());
    }

    protected <T> byte[] writeResponse(T response) {
        return Try.of(() -> objectMapper.writeValueAsBytes(response))
                .getOrElseThrow(ApplicationExceptions.invalidRequest());
    }
}
