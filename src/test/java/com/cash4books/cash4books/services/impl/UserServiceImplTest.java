package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.users.ForgotPasswordDto;
import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.UserRepository;
import com.cash4books.cash4books.services.SessionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WithMockUser
public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Mock
    UserRepository userRepository;

    @Mock
    SessionServiceImpl sessionServiceImpl;

    @Mock
    HttpServletRequest httpServletRequest;



    @Test
    public void createUserTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(null);
        when(userRepository.save(Mockito.any(Users.class))).thenReturn(Mockito.any(Users.class));
        Assert.assertNotNull(userServiceImpl.createUser(user));
    }

    @Test
    public void createExistingUserTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
        when(userRepository.save(Mockito.any(Users.class))).thenReturn(Mockito.any(Users.class));
        Exception exception = assertThrows(Exception.class, () -> {
           userServiceImpl.createUser(user);
        });
        Assert.assertEquals("User already present",exception.getMessage());
    }

    @Test
    public void createUserMissingValueTest() throws Exception {
        Users user = new Users();
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(null);
        when(userRepository.save(Mockito.any(Users.class))).thenReturn(Mockito.any(Users.class));
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.createUser(user);
        });
        Assert.assertEquals("Basic details not present",exception.getMessage());
    }

    @Test
    public void updateUserTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user.getEmail());
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
//        when(userRepository.save(user)).thenReturn(Mockito.any(Users.class));
        Assert.assertNotNull(userServiceImpl.updateUserProfile(user,httpServletRequest,"dummy"));
    }


    @Test
    public void updateInvalidUserTest() throws Exception {
        Users user = null;
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn("dummy");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.updateUserProfile(user,httpServletRequest,"dummy");
        });
        Assert.assertEquals("User Not Found",exception.getMessage());
    }

    @Test
    public void updateUserFailureTest() throws Exception {
        Users user = null;
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(null);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.updateUserProfile(user,httpServletRequest,"dummy");
        });
        Assert.assertEquals("User Not Logged In",exception.getMessage());
    }

    @Test
    public void getUserProfileTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user.getEmail());
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
        Assert.assertNotNull(userServiceImpl.getUserProfile(httpServletRequest,"dummy"));
    }

    @Test
    public void getInvalidUserTest() throws Exception {
        Users user = null;
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn("dummy");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.getUserProfile(httpServletRequest,"dummy");
        });
        Assert.assertEquals("User Not Found",exception.getMessage());
    }

    @Test
    public void getUserFailureTest() throws Exception {
        Users user = null;
        when(sessionServiceImpl.getSessionValidation(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(null);
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.getUserProfile(httpServletRequest,"dummy");
        });
        Assert.assertEquals("User Not Logged In",exception.getMessage());
    }

    @Test
    public void userAuthTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        UsersLoginDto usersLoginDto = new UsersLoginDto();
        usersLoginDto.setEmail("sample2@utdallas.edu");
        usersLoginDto.setPassword("test12");
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.getSession().setAttribute("USER_SESSION_ATTRIBUTES","dummy");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
        when(sessionServiceImpl.createSession(Mockito.any(UsersLoginDto.class),Mockito.any(HttpServletRequest.class))).thenReturn(user.getEmail());
        Assert.assertEquals("sample2@utdallas.edu",userServiceImpl.authenticateUser(usersLoginDto,request));
    }

    @Test
    public void userAuthWrongPasswordTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        UsersLoginDto usersLoginDto = new UsersLoginDto();
        usersLoginDto.setEmail("sample2@utdallas.edu");
        usersLoginDto.setPassword("wrong");
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.getSession().setAttribute("USER_SESSION_ATTRIBUTES","dummy");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
        when(sessionServiceImpl.createSession(Mockito.any(UsersLoginDto.class),Mockito.any(HttpServletRequest.class))).thenReturn(user.getEmail());
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.authenticateUser(usersLoginDto,request);
        });
        Assert.assertEquals("Password Error",exception.getMessage());
    }

    @Test
    public void userAuthInvalidUserTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        UsersLoginDto usersLoginDto = new UsersLoginDto();
        usersLoginDto.setEmail("sample2@utdallas.edu");
        usersLoginDto.setPassword("test12");
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.getSession().setAttribute("USER_SESSION_ATTRIBUTES","dummy");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.authenticateUser(usersLoginDto,request);
        });
        Assert.assertEquals("User Not Found",exception.getMessage());
    }

    @Test
    public void forgotPasswordTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("new_test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
        forgotPasswordDto.setEmail("sample2@utdallas.edu");
        forgotPasswordDto.setQuestion("Pet name");
        forgotPasswordDto.setAnswer("test");
        forgotPasswordDto.setPassword("new_test12");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
        Assert.assertNotNull(userServiceImpl.forgotPassword(forgotPasswordDto));

   }

    @Test
    public void forgotPasswordInvalidQnTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("new_test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
        forgotPasswordDto.setEmail("sample2@utdallas.edu");
        forgotPasswordDto.setQuestion("City name");
        forgotPasswordDto.setAnswer("test");
        forgotPasswordDto.setPassword("new_test12");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(user);
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.forgotPassword(forgotPasswordDto);
        });
        Assert.assertEquals("User Question and Answer do not match",exception.getMessage());

    }

    @Test
    public void forgotPasswordInvalidUserTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("new_test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
        forgotPasswordDto.setEmail("sample2@utdallas.edu");
        forgotPasswordDto.setQuestion("Pet name");
        forgotPasswordDto.setAnswer("test");
        forgotPasswordDto.setPassword("new_test12");
        when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> {
            userServiceImpl.forgotPassword(forgotPasswordDto);
        });
        Assert.assertEquals("User Not Found",exception.getMessage());


    }
}
