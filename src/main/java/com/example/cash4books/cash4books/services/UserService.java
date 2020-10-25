package com.example.cash4books.cash4books.services;

import com.example.cash4books.cash4books.dto.users.UsersLoginDto;
import com.example.cash4books.cash4books.entity.Users;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    Users updateUserProfile(Users newUserDetails);
    Users getUserProfile(Users userDetails);
    String authenticateUser(UsersLoginDto usersLoginDto, HttpServletRequest request);
    String createUser(Users newUserDetails);

}

