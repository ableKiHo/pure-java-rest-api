package com.umsign.app.api.user.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.umsign.app.api.Constants;
import com.umsign.app.api.Handler;
import com.umsign.app.api.ResponseEntity;
import com.umsign.app.api.StatusCode;
import com.umsign.app.errors.ApplicationExceptions;
import com.umsign.app.errors.GlobalExceptionHandler;
import com.umsign.domain.user.User;
import com.umsign.domain.user.UserService;

import java.io.OutputStream;
import java.util.List;

public class AllUserListHandler extends Handler {
    private final UserService userService;

    public AllUserListHandler(UserService userService, ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.userService = userService;
    }

    @Override
    protected void excute(HttpExchange exchange) throws Exception {
        byte[] response;
        if("GET".equals(exchange.getRequestMethod())) {
            ResponseEntity e = doPost();
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

    private ResponseEntity<List<User>> doPost() {
        List<User> users = userService.allUsers();

        return new ResponseEntity<>(users, getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);

    }
}
