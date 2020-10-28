package com.cash4books.cash4books.controller;

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
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = {"/createUser"},produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> createUser(@RequestBody Users newUserDetails){
        try{
            Users userdetails=userService.createUser(newUserDetails);
            return new ResponseEntity(userdetails,HttpStatus.ACCEPTED);
        }
        catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = {"/login"},produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> login(@RequestBody UsersLoginDto usersLoginDto, HttpServletRequest request){
        try {
            List<String> userSessionInfo = userService.authenticateUser(usersLoginDto, request);
            return new ResponseEntity(userSessionInfo,HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = {"/getProfile"}, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> getProfile(HttpServletRequest request,@RequestHeader (name="Token") String token,@RequestHeader (name="email") String userEmail){
        try {
            Users getUserProfile = userService.getUserProfile(request,token,userEmail);
            return new ResponseEntity(getUserProfile,HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = {"/updateProfile"}, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Users> updateProfile(@RequestBody Users newUserDetails, HttpServletRequest request, @RequestHeader (name="Token") String token, @RequestHeader (name="email") String userEmail){
        try {
            Users updatedDetails= userService.updateUserProfile(newUserDetails,request,token,userEmail);
            return new ResponseEntity(updatedDetails,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = {"/logout"}, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProfile(HttpServletRequest request){
       userService.logoutUser(request);
       return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }



}
