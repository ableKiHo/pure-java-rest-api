package com.umsign.app.api.user;

import lombok.Value;

@Value
public class RegistrationRequest {
    String login;
    String password;
}
