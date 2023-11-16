package com.oredata.bookstoreapi.service;

import com.oredata.bookstoreapi.entity.User;

public interface UserService {
    User signUp(User user);
    String login(String email, String password);
    User getUserById(Long userId);
}
