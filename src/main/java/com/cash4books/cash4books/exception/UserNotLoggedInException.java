package com.cash4books.cash4books.exception;

public class UserNotLoggedInException extends Exception{
    private static final String DEFAULT_MESSAGE = "User Not Logged In";

    public UserNotLoggedInException() {
        super(DEFAULT_MESSAGE);
    }

}
