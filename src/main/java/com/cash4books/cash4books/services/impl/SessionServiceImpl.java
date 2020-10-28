package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {

    Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);



        public boolean getSessionValidation(HttpServletRequest request,String token,String userEmail) {
            List<String> sessionDetails = (List<String>) request.getSession().getAttribute("USER_SESSION_ATTRIBUTES");
            if (sessionDetails!=null) {
                if(sessionDetails.contains(token) && sessionDetails.contains(userEmail)){
                    return true;
                }
            }
            return false;
        }

        public List<String> createSession(UsersLoginDto usersLoginDto, HttpServletRequest request) {
            @SuppressWarnings("unchecked")
            List<String> attributes = (List<String>) request.getSession().getAttribute("USER_SESSION_ATTRIBUTES");
            if (attributes == null) {
                attributes=new ArrayList<>();
                String token= UUID.randomUUID().toString();
                attributes.add(token);
                attributes.add(usersLoginDto.getEmail());
                request.getSession().setAttribute("USER_SESSION_ATTRIBUTES", attributes);
            }
            return attributes;
        }

        public void destroySession(HttpServletRequest request) {
            request.getSession().invalidate();
        }
}
