package com.posd.expensemanager.service.user;

import com.posd.expensemanager.model.User;

import java.util.Optional;

public interface AuthenticationService {

    Boolean register(String username, String password);

    Optional<User> login(String username, String password);

    String encodePassword(String password);

}
