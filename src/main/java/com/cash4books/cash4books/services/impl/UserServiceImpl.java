package com.cash4books.cash4books.services.impl;

import com.cash4books.cash4books.dto.users.UsersLoginDto;
import com.cash4books.cash4books.entity.Users;
import com.cash4books.cash4books.services.UserService;
import com.cash4books.cash4books.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private  SessionServiceImpl sessionService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String createUser(Users newUserDetails){
        try{
            if(userRepository.findUserByEmail(newUserDetails.getEmail())==null){
                userRepository.save(newUserDetails);
                return "Created New User";
            }
            return "User Already Exists";
        }
        catch (Exception e){
            return "Failed To Create User";
        }
    }

    @Override
    public Users updateUserProfile(Users newUserDetails) {
        try {
            Users existingUserDetails = userRepository.findUserByEmail(newUserDetails.getEmail());
            if (existingUserDetails != null) {
                existingUserDetails = newUserDetails;
                userRepository.save(existingUserDetails);
                return existingUserDetails;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Users getUserProfile (Users userDetails) {
        try {
            Users getUserProfile = userRepository.findUserByEmail(userDetails.getEmail());
            if (getUserProfile != null) {
                return getUserProfile;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String authenticateUser(UsersLoginDto usersLoginDto, HttpServletRequest request){
        try{
            Users userDetails=userRepository.findUserByEmail(usersLoginDto.getEmail());
            if(userDetails!=null){
                if(userDetails.getPassword().equals(usersLoginDto.getPassword())){
                    sessionService.createSession(usersLoginDto,request);
                    return usersLoginDto.getEmail();
                }
                else
                {
                    return null;
                }
            }
            else{
                return null;
            }
        }
        catch (Exception e){
            return null;
        }
    }

    public void logoutUser(HttpServletRequest request){
        sessionService.destroySession(request);
    }

}
