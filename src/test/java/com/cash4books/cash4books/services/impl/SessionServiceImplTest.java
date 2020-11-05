package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.users.UsersLoginDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@WithMockUser
public class SessionServiceImplTest {
    @InjectMocks
    SessionServiceImpl sessionServiceImpl;

//    @Mock
//    HttpServletRequest request;

    @Test
    public void createSessionTest() throws UnsupportedEncodingException {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        UsersLoginDto usersLoginDto = new UsersLoginDto();
        usersLoginDto.setEmail("sample2@utdallas.edu");
        usersLoginDto.setPassword("test12");
        request.getSession().setAttribute("USER_SESSION_ATTRIBUTES",null);
        String result = sessionServiceImpl.createSession(usersLoginDto, request);
        Assert.assertNotNull(result);

    }

    @Test
    public void sessionValidationTest() throws UnsupportedEncodingException {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        UsersLoginDto usersLoginDto = new UsersLoginDto();
        usersLoginDto.setEmail("sample2@utdallas.edu");
        usersLoginDto.setPassword("test12");
        String encoded = "c2FtcGxlMkB1dGRhbGxhcy5lZHU=";
        String decoded = "sample2@utdallas.edu";
        request.getSession().setAttribute("USER_SESSION_ATTRIBUTES",encoded);
        String result = sessionServiceImpl.getSessionValidation(request,encoded);
        Assert.assertEquals(decoded,result);
    }

    @Test
    public void sessionValidationFailureTest() throws UnsupportedEncodingException {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        UsersLoginDto usersLoginDto = new UsersLoginDto();
        usersLoginDto.setEmail("sample2@utdallas.edu");
        usersLoginDto.setPassword("test12");
        String encoded = "c2FtcGxlMkB1dGRhbGxhcy5lZHU=";
        String decoded = "sample2@utdallas.edu";
        request.getSession().setAttribute("USER_SESSION_ATTRIBUTES","dummy");
        String result = sessionServiceImpl.getSessionValidation(request,encoded);
        Assert.assertNull(result);
    }

    @Test
    public void destroySessionTest(){
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.getSession().setAttribute("USER_SESSION_ATTRIBUTES","dummy");
        sessionServiceImpl.destroySession(request);
        HttpSession session = request.getSession(false);
        Assert.assertNull(session);
    }

}
