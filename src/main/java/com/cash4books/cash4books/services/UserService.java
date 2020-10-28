package com.cash4books.cash4books.services;

import com.cash4books.cash4books.dto.users.ForgotPasswordDto;
import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.entity.Users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    Users updateUserProfile(Users newUserDetails,HttpServletRequest request,String token) throws Exception;
    Users getUserProfile(HttpServletRequest request, String token) throws Exception;
    String authenticateUser(UsersLoginDto usersLoginDto, HttpServletRequest request) throws Exception;
    Users createUser(Users newUserDetails) throws Exception;
    Users forgotPassword(ForgotPasswordDto forgotPasswordDto)throws Exception;
    void logoutUser(HttpServletRequest request);



}

