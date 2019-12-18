package com.umsign.data.user;

import com.umsign.domain.user.NewUser;
import com.umsign.domain.user.User;
import com.umsign.domain.user.UserRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
    private static final Map<String, User> USERS_STORE = new ConcurrentHashMap();
    @Override
    public String create(NewUser newUser) {
        String id = UUID.randomUUID().toString();
        User user = User.builder()
                .id(id)
                .login(newUser.getLogin())
                .password(newUser.getPassword())
                .build();
        USERS_STORE.put(newUser.getLogin(), user);
        return id;
    }
}
