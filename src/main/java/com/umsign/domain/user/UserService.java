package com.umsign.domain.user;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String create(NewUser user) {
        return userRepository.create(user);
    }

    public List<User> allUsers() {
        return userRepository.allUsers();
    }

    public User findUser(String login) {
        return userRepository.findUser(login);
    }
}
