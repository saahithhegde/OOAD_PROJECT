package com.example.cash4books.cash4books.controller;

import com.example.cash4books.cash4books.dto.users.UsersLoginDto;
import com.example.cash4books.cash4books.entity.Users;
import com.example.cash4books.cash4books.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = {"/createUser"},produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createUser(@RequestBody Users newUserDetails){
        return userService.createUser(newUserDetails);
    }

    @PostMapping(value = {"/login"},produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public String userLogin(@RequestBody UsersLoginDto usersLoginDto, HttpServletRequest request){
        return userService.authenticateUser(usersLoginDto,request);
    }

    @PostMapping(value = {"/getProfile"}, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Users getProfile(@RequestBody Users userDetails){
        Users getUserProfile= userService.getUserProfile(userDetails);
        return getUserProfile;
    }

    @PostMapping(value = {"/updateProfile"}, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Users updateProfile(@RequestBody Users newUserDetails){
        Users updateUserProfile= userService.updateUserProfile(newUserDetails);
        return updateUserProfile;
    }



}
