package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.services.SessionService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {

    Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

        @Override
        public String getSessionValidation(HttpServletRequest request,String token) throws UnsupportedEncodingException {
            byte[] dectryptArray = token.getBytes();
            byte[] decarray = Base64.decodeBase64(dectryptArray);
            String decryptedEmail = new String(decarray,"UTF-8");
            String sessionDetails = (String) request.getSession().getAttribute("USER_SESSION_ATTRIBUTES");
            if (sessionDetails.equals(token)) {
                logger.info("valid session");
                return decryptedEmail;
            }
            return null;
        }

        @Override
        public String createSession(UsersLoginDto usersLoginDto, HttpServletRequest request) throws UnsupportedEncodingException {
            @SuppressWarnings("unchecked")
            String encryptedEmail = (String) request.getSession().getAttribute("USER_SESSION_ATTRIBUTES");
            if (encryptedEmail == null) {
                byte[] encryptArray = Base64.encodeBase64(usersLoginDto.getEmail().getBytes());
                encryptedEmail = new String(encryptArray,"UTF-8");
                logger.info("set session attributes");
                request.getSession().setAttribute("USER_SESSION_ATTRIBUTES", encryptedEmail);
            }
            return encryptedEmail;
        }

        @Override
        public void destroySession(HttpServletRequest request) {
            logger.info("destroyed session attributes");
            request.getSession().invalidate();
        }
}
