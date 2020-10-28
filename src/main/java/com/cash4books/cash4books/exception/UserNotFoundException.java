package com.cash4books.cash4books.exception;

public class UserNotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "User Not Found";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
