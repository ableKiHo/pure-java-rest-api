package com.umsign.app.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.umsign.app.api.Constants;
import com.umsign.app.api.Handler;
import com.umsign.app.api.ResponseEntity;
import com.umsign.app.api.StatusCode;
import com.umsign.app.errors.ApplicationExceptions;
import com.umsign.app.errors.GlobalExceptionHandler;
import com.umsign.domain.user.NewUser;
import com.umsign.domain.user.UserService;

import java.io.InputStream;
import java.io.OutputStream;

public class RegisterationHandler extends Handler {
    private final UserService userService;

    public RegisterationHandler(UserService userService, ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.userService = userService;
    }

    @Override
    protected void excute(HttpExchange exchange) throws Exception {
        byte[] response = null;
        if("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity e = doPost(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        } else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()
            ).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private ResponseEntity<RegistrationResponse> doPost(InputStream is) {
        RegistrationRequest registerRequest = super.readRequest(is, RegistrationRequest.class);

        NewUser user = NewUser.builder()
                .login(registerRequest.getLogin())
                .password(PasswordEncoder.encode(registerRequest.getPassword()))
                .build();

        String userId = userService.create(user);

        RegistrationResponse response = new RegistrationResponse(userId);
        return new ResponseEntity<>(response, getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);

    }
}
