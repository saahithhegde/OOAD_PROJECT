package com.cash4books.cash4books.controller;

import com.cash4books.cash4books.dto.cart.UserCartDto;
import com.cash4books.cash4books.dto.orders.BooksOrderDto;
import com.cash4books.cash4books.dto.users.ForgotPasswordDto;
import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.entity.UserPaymentTypes;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.exception.UserNotLoggedInException;
import com.cash4books.cash4books.services.impl.BookServiceImpl;
import com.cash4books.cash4books.services.impl.PaymentServiceImpl;
import com.cash4books.cash4books.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BookServiceImpl bookService;

    @Autowired
    PaymentServiceImpl paymentServiceImpl;

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


    @GetMapping(value = {"/logout"})
    public String logout(HttpServletRequest request) {
        userService.logoutUser(request);
        return "logged Out";
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

    @GetMapping(value = {"/sellHistory"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BooksOrderDto>> getSellHistory(HttpServletRequest request, @RequestHeader(name = "Token") String token) {
        try {
            List<BooksOrderDto> sellerHistory = bookService.getSellHistory(request,token);
            return new ResponseEntity(sellerHistory,HttpStatus.OK);
        } catch (UserNotLoggedInException e) {
            return new ResponseEntity("User not logged in",HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("Failed to get seller history",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/buyHistory"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BooksOrderDto>> getBuyHistory(HttpServletRequest request, @RequestHeader(name = "Token") String token) {
        try {
            List<BooksOrderDto> buyerHistory = bookService.getBuyHistory(request,token);
            return new ResponseEntity(buyerHistory,HttpStatus.OK);
        } catch (UserNotLoggedInException e) {
            return new ResponseEntity("User not logged in",HttpStatus.BAD_REQUEST);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity("Failed to get seller history",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPaymentTypes> addUserPaymentTypes(@RequestBody UserPaymentTypes userPaymentTypes , HttpServletRequest request, @RequestHeader(name = "Token") String token){
        try {
            UserPaymentTypes userPaymentTypesInfo = paymentServiceImpl.addUserPaymentsType(request,token,userPaymentTypes);
            return new ResponseEntity(userPaymentTypesInfo, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPaymentTypes> deleteUserPaymentTypes(@RequestBody UserPaymentTypes userPaymentTypes , HttpServletRequest request, @RequestHeader(name = "Token") String token){
        try {
            UserPaymentTypes userPaymentTypesInfo = paymentServiceImpl.deleteUserPaymentsType(request,token,userPaymentTypes);
            return new ResponseEntity(userPaymentTypesInfo, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value="/getUserPaymentTypes" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserCartDto>> getUserCart(HttpServletRequest request, @RequestHeader(name = "Token") String token){
        try {
            List<UserPaymentTypes> userPaymentInfo = paymentServiceImpl.getUserPayments(request,token);
            return new ResponseEntity(userPaymentInfo, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
