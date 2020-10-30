package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.users.ForgotPasswordDto;
import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = {"/createUser"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> createUser(@RequestBody Users newUserDetails) {
        try {
            Users userdetails = userService.createUser(newUserDetails);
            return new ResponseEntity(userdetails, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = {"/login"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody UsersLoginDto usersLoginDto, HttpServletRequest request) {
        try {
            Map<String, Object> response = new HashMap<>();
            String userSessionInfo = userService.authenticateUser(usersLoginDto, request);
            response.put("token",userSessionInfo);
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = {"/getProfile"}, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> getProfile(HttpServletRequest request, @RequestHeader(name = "Token") String token) {
        try {
            Users getUserProfile = userService.getUserProfile(request, token);
            return new ResponseEntity(getUserProfile, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = {"/updateProfile"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> updateProfile(@RequestBody Users newUserDetails, HttpServletRequest request, @RequestHeader(name = "Token") String token) {
        try {
            Users updatedDetails = userService.updateUserProfile(newUserDetails, request, token);
            return new ResponseEntity(updatedDetails, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping(value = {"/logout"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProfile(HttpServletRequest request) {
        userService.logoutUser(request);
        return new ResponseEntity<>("Logged out successfully", HttpStatus.ACCEPTED);
    }

    @PostMapping(value = {"/forgotPassword"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProfile(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        try {
            Users users = userService.forgotPassword(forgotPasswordDto);
            if (users !=null) {
                return new ResponseEntity(users, HttpStatus.ACCEPTED);
            }
            return new ResponseEntity("failed", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
