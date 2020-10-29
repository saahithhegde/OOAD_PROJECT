package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.UserRepository;
import com.cash4books.cash4books.services.SessionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

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
}
