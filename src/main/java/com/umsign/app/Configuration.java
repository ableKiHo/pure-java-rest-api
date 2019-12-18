package com.umsign.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umsign.app.errors.GlobalExceptionHandler;
import com.umsign.data.user.InMemoryUserRepository;
import com.umsign.domain.user.UserRepository;
import com.umsign.domain.user.UserService;

class Configuration {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final UserRepository USER_REPOSITORY = new InMemoryUserRepository();
    private static final UserService USER_SERVICE = new UserService(USER_REPOSITORY);
    private static final GlobalExceptionHandler GLOBAL_EXCEPTION_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    static UserRepository getUserRepository() {
        return USER_REPOSITORY;
    }

    static UserService getUserService() {
        return USER_SERVICE;
    }

    public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_EXCEPTION_HANDLER;
    }
}
