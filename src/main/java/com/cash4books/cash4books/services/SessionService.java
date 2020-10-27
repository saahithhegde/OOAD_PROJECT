package com.cash4books.cash4books.services;

import com.cash4books.cash4books.dto.users.UsersLoginDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface SessionService {
    String createSession(UsersLoginDto usersLoginDto, HttpServletRequest request);
    String getSession(HttpSession session);
    void destroySession(HttpServletRequest request);
}
