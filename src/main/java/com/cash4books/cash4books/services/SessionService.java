package com.cash4books.cash4books.services;

import com.cash4books.cash4books.dto.users.UsersLoginDto;

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface SessionService {
    String createSession(UsersLoginDto usersLoginDto, HttpServletRequest request) throws UnsupportedEncodingException;
    String getSessionValidation(HttpServletRequest request,String token) throws UnsupportedEncodingException;
    void destroySession(HttpServletRequest request);
}
