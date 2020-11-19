package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.users.ForgotPasswordDto;
import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.exception.UserNotFoundException;
import com.cash4books.cash4books.exception.UserNotLoggedInException;
import com.cash4books.cash4books.services.UserService;
import com.cash4books.cash4books.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private  SessionServiceImpl sessionService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Users createUser(Users newUserDetails) throws Exception{
            if(userRepository.findUserByEmail(newUserDetails.getEmail())==null){
                if(newUserDetails.getEmail()!=null && newUserDetails.getPassword()!=null && newUserDetails.getQuestion()!=null && newUserDetails.getAnswer()!=null ) {
                    userRepository.save(newUserDetails);
                    logger.info("Saved new user");
                    return newUserDetails;
                }
                else{
                    logger.error("Required field not present");
                    throw new Exception("Basic details not present");
                }
            }
            else {
                logger.error("User already present");
                throw new Exception("User already present");
            }
    }

    @Override
    public Users updateUserProfile(Users newUserDetails, HttpServletRequest request, String token) throws Exception {
        String email=sessionService.getSessionValidation(request,token);
        if(email!=null) {
            Users existingUserDetails = userRepository.findUserByEmail(email);
            if (existingUserDetails != null) {
                existingUserDetails = newUserDetails;
                userRepository.save(existingUserDetails);
                logger.info("Updated user profile details");
                return existingUserDetails;
            } else {
                logger.error("User not found");
                throw new UserNotFoundException();
            }
        }
        else{
            logger.error("User not logged in");
            throw new UserNotLoggedInException();
        }
    }

    @Override
    public Users getUserProfile (HttpServletRequest request,String token) throws Exception{
            String email=sessionService.getSessionValidation(request,token);
            if(email!=null) {
                Users getUserProfile = userRepository.findUserByEmail(email);
                if (getUserProfile != null) {
                    logger.info("Retrieved user successfully");
                    return getUserProfile;
                }
                else {
                    logger.error("User not found");
                    throw new UserNotFoundException();
                }
            }
            else{
                logger.error("User not logged in");
                throw new UserNotLoggedInException();
            }
    }

    @Override
    public String authenticateUser(UsersLoginDto usersLoginDto, HttpServletRequest request) throws Exception{
            Users userDetails = userRepository.findUserByEmail(usersLoginDto.getEmail());
            if (userDetails != null) {
                if (userDetails.getPassword().equals(usersLoginDto.getPassword())) {
                    logger.info("Created new session and Authenticated the user");
                    return sessionService.createSession(usersLoginDto, request);
                } else {
                    logger.error("Wrong Password");
                    throw new Exception("Password Error");
                }
            } else {
                logger.error("User not found");
                throw new UserNotFoundException();
            }
    }

    @Override
    public void logoutUser(HttpServletRequest request){
        logger.info("User logged out");
        sessionService.destroySession(request);
    }

    @Override
    public Users forgotPassword(ForgotPasswordDto forgotPasswordDto)throws Exception{
        Users userDetails=userRepository.findUserByEmail(forgotPasswordDto.getEmail());
        if(userDetails!=null){
            if(userDetails.getQuestion().equals(forgotPasswordDto.getQuestion()) && userDetails.getAnswer().equals(forgotPasswordDto.getAnswer())){
                userDetails.setPassword(forgotPasswordDto.getPassword());
                userRepository.save(userDetails);
                userDetails.setPassword("");
                userDetails.setQuestion("");
                userDetails.setAnswer("");
                logger.info("New password updated");
                return userDetails;
            }
            else{
                logger.error("User Question and Answer do not match");
                throw new Exception("User Question and Answer do not match");
            }
        }
        else {
            logger.error("user not found");
            throw new UserNotFoundException();
        }
    }

}
