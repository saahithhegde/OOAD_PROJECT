package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.users.ForgotPasswordDto;
import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.repository.UserRepository;
import com.cash4books.cash4books.services.impl.SessionServiceImpl;
import com.cash4books.cash4books.services.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UsersController.class)
@WithMockUser
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    SessionServiceImpl sessionService;

    @MockBean
    UserRepository userRepository;

    @Test
    public void shouldCreateUser() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        when(userService.createUser(Mockito.any(Users.class))).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\":\"sample2@utdallas.edu\", \"password\" : \"test12\", \"question\" : \"Pet name\", \"answer\" : \"test\",  \"phoneNo\" : \"1241342\", \"address\" : \"Renner Rd\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.question").exists())
                .andExpect(jsonPath("$.answer").exists())
                .andExpect(jsonPath("$.phoneNo").exists())
                .andExpect(jsonPath("$.address").exists())
                .andExpect(jsonPath("$.email").value("sample2@utdallas.edu"))
                .andExpect(jsonPath("$.password").value("test12"))
                .andExpect(jsonPath("$.question").value("Pet name"))
                .andExpect(jsonPath("$.answer").value("test"))
                .andExpect(jsonPath("$.phoneNo").value("1241342"))
                .andExpect(jsonPath("$.address").value("Renner Rd"))
                .andDo(print());
    }

    @Test(expected = Exception.class)
    public void createUserExceptionTest() throws Exception {
        doThrow(Exception.class).when(userService.createUser(Mockito.any(Users.class)));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\":\"sample2@utdallas.edu\", \"password\" : \"test12\", \"question\" : \"Pet name\", \"answer\" : \"test\", \"userName\" : \"sample2\", \"phoneNo\" : \"1241342\", \"address\" : \"Renner Rd\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    public void loginTest() throws Exception {
        UsersLoginDto usersLoginDto = new UsersLoginDto();
        usersLoginDto.setEmail("sample2@utdallas.edu");
        usersLoginDto.setPassword("test12");
        when(userService.authenticateUser(Mockito.any(UsersLoginDto.class),Mockito.any(HttpServletRequest.class))).thenReturn("user");
        Assert.assertTrue(
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\":\"sample2@utdallas.edu\", \"password\" : \"test12\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("user"));
    }

    @Test
    public void logoutTest() throws Exception {
        UsersLoginDto usersLoginDto = new UsersLoginDto();
        usersLoginDto.setEmail("sample2@utdallas.edu");
        usersLoginDto.setPassword("test12");
        doNothing().when(userService).logoutUser(Mockito.any(HttpServletRequest.class));
        Assert.assertTrue(
                mockMvc.perform(MockMvcRequestBuilders.get("/api/users/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
                        .contains("Logged out successfully"));
    }

    @Test
    public void getProfileTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        when(userService.getUserProfile(Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/getProfile")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","sample2@utdallas.edu")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.question").exists())
                .andExpect(jsonPath("$.answer").exists())
                .andExpect(jsonPath("$.phoneNo").exists())
                .andExpect(jsonPath("$.address").exists())
                .andExpect(jsonPath("$.email").value("sample2@utdallas.edu"))
                .andExpect(jsonPath("$.password").value("test12"))
                .andExpect(jsonPath("$.question").value("Pet name"))
                .andExpect(jsonPath("$.answer").value("test"))
                .andExpect(jsonPath("$.phoneNo").value("1241342"))
                .andExpect(jsonPath("$.address").value("Renner Rd"))
                .andDo(print());
    }

    @Test
    public void updateProfileTest() throws Exception {
        Users user = new Users();
        user.setEmail("sample2@utdallas.edu");
        user.setPassword("test12");
        user.setAddress("Renner Rd");
        user.setQuestion("Pet name");
        user.setAnswer("test");
        user.setPhoneNo("1241342");
        when(userService.updateUserProfile(Mockito.any(Users.class),Mockito.any(HttpServletRequest.class),Mockito.anyString())).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/updateProfile")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Token","sample2@utdallas.edu")
                .content("{ \"email\":\"sample2@utdallas.edu\", \"password\" : \"test12\", \"question\" : \"Pet name\", \"answer\" : \"test\", \"phoneNo\" : \"1241342\", \"address\" : \"Renner Rd\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.password").exists())
                .andExpect(jsonPath("$.question").exists())
                .andExpect(jsonPath("$.answer").exists())
                .andExpect(jsonPath("$.phoneNo").exists())
                .andExpect(jsonPath("$.address").exists())
                .andExpect(jsonPath("$.email").value("sample2@utdallas.edu"))
                .andExpect(jsonPath("$.password").value("test12"))
                .andExpect(jsonPath("$.question").value("Pet name"))
                .andExpect(jsonPath("$.answer").value("test"))
                .andExpect(jsonPath("$.phoneNo").value("1241342"))
                .andExpect(jsonPath("$.address").value("Renner Rd"))
                .andDo(print());
    }

    @Test
    public void forgotPasswordPositiveTest() throws Exception {
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
        when(userService.forgotPassword(Mockito.any(ForgotPasswordDto.class))).thenReturn(user);
        Assert.assertTrue(mockMvc.perform(MockMvcRequestBuilders.post("/api/users/forgotPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\":\"sample2@utdallas.edu\", \"password\" : \"new_test12\", \"question\" : \"Pet name\", \"answer\" : \"test\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("successfully changed password"));
    }


    @Test
    public void forgotPasswordNegativeTest() throws Exception {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
        forgotPasswordDto.setEmail("sample2@utdallas.edu");
        forgotPasswordDto.setQuestion("Pet name");
        forgotPasswordDto.setAnswer("test");
        forgotPasswordDto.setPassword("new_test12");
        when(userService.forgotPassword(Mockito.any(ForgotPasswordDto.class))).thenReturn(null);
        Assert.assertTrue(mockMvc.perform(MockMvcRequestBuilders.post("/api/users/forgotPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\":\"sample2@utdallas.edu\", \"password\" : \"new_test12\", \"question\" : \"Pet name\", \"answer\" : \"test\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("failed"));
    }


}
