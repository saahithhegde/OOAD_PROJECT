package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.services.SessionService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class SessionServiceImpl implements SessionService {



        public String getSession(HttpSession session) {
            @SuppressWarnings("unchecked")
            String email = (String) session.getAttribute("USER_SESSION_ATTRIBUTES");
            if (email == null) {
                return null;
            }
            return email;
        }

        public String createSession(UsersLoginDto usersLoginDto, HttpServletRequest request) {
            @SuppressWarnings("unchecked")
            String email = (String) request.getSession().getAttribute("USER_SESSION_ATTRIBUTES");
            if (email == null) {
                request.getSession().setAttribute("USER_SESSION_ATTRIBUTES", usersLoginDto.getEmail());
            }
            return email;
        }

        public void destroySession(HttpServletRequest request) {
            request.getSession().invalidate();
        }
}
