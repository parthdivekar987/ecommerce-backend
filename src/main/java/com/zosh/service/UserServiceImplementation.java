package com.zosh.service;

import com.zosh.config.JwtProvider;
import com.zosh.exception.UserException;
import com.zosh.model.User;
import com.zosh.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserServiceImplementation implements UserService {

    private UserRepository userRepository;
    private JwtProvider jwtProvider;


    public UserServiceImplementation(UserRepository userRepository,JwtProvider jwtProvider)
    {
        this.userRepository=userRepository;
        this.jwtProvider=jwtProvider;
    }
    @Override
    public User findUserById(long userId) throws UserException {

        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent())
        {
            return user.get();
        }
    throw  new UserException("User Not Found wiht id:"+userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {

        String email=jwtProvider.getEMailFromToken(jwt);
        User user=userRepository.findByEmail(email);

        if(user==null)
        {
            throw new UserException("User Not Found with Email !"+email);
        }
        return user;
    }
}
