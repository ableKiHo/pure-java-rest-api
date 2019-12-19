package com.umsign.data.user;

import com.umsign.domain.user.NewUser;
import com.umsign.domain.user.User;
import com.umsign.domain.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryUserRepository implements UserRepository {
    /*
    * ConcurrentHashMap feature
    * 1. thread-safe
    * 2. not allowed null in key and value
    * 3. putIfAbsent method
    * */
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

    @Override
    public List<User> allUsers() {
        return new ArrayList<>(USERS_STORE.values());
    }

    @Override
    public User findUser(String login) {
        return USERS_STORE.getOrDefault(login, null);
    }
}
