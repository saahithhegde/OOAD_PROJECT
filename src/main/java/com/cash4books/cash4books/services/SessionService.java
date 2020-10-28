package com.cash4books.cash4books.services;

import com.cash4books.cash4books.dto.users.UsersLoginDto;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface SessionService {
    List<String> createSession(UsersLoginDto usersLoginDto, HttpServletRequest request);
    boolean  getSessionValidation(HttpServletRequest request,String token,String userEmail);
    void destroySession(HttpServletRequest request);
}
