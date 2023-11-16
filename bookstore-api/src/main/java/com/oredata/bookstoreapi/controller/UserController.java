package com.oredata.bookstoreapi.controller;

import com.oredata.bookstoreapi.entity.User;
import com.oredata.bookstoreapi.exception.ExistingUserException;
import com.oredata.bookstoreapi.exception.UserNotFoundException;
import com.oredata.bookstoreapi.exception.WrongPasswordException;
import com.oredata.bookstoreapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users", produces = "application/json", consumes = "application/json")
public class UserController {

    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        User newUser = userService.signUp(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String jwtToken = userService.login(user.getEmail(), user.getPassword());
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    @ExceptionHandler(ExistingUserException.class)
    public ResponseEntity<String> handleExistingUserException(ExistingUserException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<String> handleWrongPasswordException(WrongPasswordException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
