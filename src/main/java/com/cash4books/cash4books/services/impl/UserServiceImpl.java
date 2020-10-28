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
                    logger.info("saved new user");
                    return newUserDetails;
                }
                else{
                    logger.error("required field not present");
                    throw new Exception("Basic details not present");
                }
            }
            else {
                logger.error("user already present");
                throw new Exception("User already present");
            }
    }

    @Override
    public Users updateUserProfile(Users newUserDetails, HttpServletRequest request, String token, String userEmail) throws Exception {
        if(sessionService.getSessionValidation(request,token,userEmail)) {
            Users existingUserDetails = userRepository.findUserByEmail(userEmail);
            if (existingUserDetails != null) {
                existingUserDetails = newUserDetails;
                userRepository.save(existingUserDetails);
                logger.info("updated user");
                return existingUserDetails;
            } else {
                logger.error("user not found");
                throw new UserNotFoundException();
            }
        }
        else{
            logger.error("user not logged in");
            throw new UserNotLoggedInException();
        }
    }

    @Override
    public Users getUserProfile (HttpServletRequest request,String token, String userEmail) throws Exception{
            if(sessionService.getSessionValidation(request,token,userEmail)) {
                Users getUserProfile = userRepository.findUserByEmail(userEmail);
                if (getUserProfile != null) {
                    logger.info("retrieved user successfully");
                    return getUserProfile;
                }
                else {
                    logger.error("user not found");
                    throw new UserNotFoundException();
                }
            }
            else{
                logger.error("user not logged in");
                throw new UserNotLoggedInException();
            }
    }

    @Override
    public List<String> authenticateUser(UsersLoginDto usersLoginDto, HttpServletRequest request) throws Exception{
            Users userDetails = userRepository.findUserByEmail(usersLoginDto.getEmail());
            if (userDetails != null) {
                if (userDetails.getPassword().equals(usersLoginDto.getPassword())) {
                    logger.info("created new session and authenticated user");
                    return sessionService.createSession(usersLoginDto, request);
                } else {
                    logger.error("password wrong");
                    throw new Exception("Password Error");
                }
            } else {
                logger.error("user not found");
                throw new UserNotFoundException();
            }
    }

    @Override
    public void logoutUser(HttpServletRequest request){
        logger.info("logged out");
        sessionService.destroySession(request);
    }

    @Override
    public Users forgotPassword(ForgotPasswordDto forgotPasswordDto)throws Exception{
        Users userDetails=userRepository.findUserByEmail(forgotPasswordDto.getEmail());
        if(userDetails!=null){
            if(userDetails.getQuestion().equals(forgotPasswordDto.getQuestion()) && userDetails.getAnswer().equals(forgotPasswordDto.getAnswer())){
                userDetails.setPassword(forgotPasswordDto.getPassword());
                userRepository.save(userDetails);
                logger.info("new password set");
                return userDetails;
            }
            else{
                throw new Exception("User Question and answers do not match");
            }
        }
        else {
            logger.error("user not found");
            throw new UserNotFoundException();
        }
    }

}
