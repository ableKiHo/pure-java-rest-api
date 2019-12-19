package com.umsign.domain.user;

import java.util.List;

public interface UserRepository {
    String create(NewUser user);
    List<User> allUsers();
    User findUser(String login);
}
