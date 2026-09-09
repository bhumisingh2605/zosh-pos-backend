package com.zosh.zosh.pos.system.service.impl;

import com.zosh.zosh.pos.system.config.JwtProvider;
import com.zosh.zosh.pos.system.exceptions.UserException;
import com.zosh.zosh.pos.system.modal.User;
import com.zosh.zosh.pos.system.repository.UserRepository;
import com.zosh.zosh.pos.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User getUserFromJwtToken(String token) throws UserException {

       String email = jwtProvider.getEmailFromToken(token);
       User user = userRepository.findByEmail(email);
       if(user == null) {
           throw new UserException("Invalid token");
       }
        return user;
    }

    @Override
    public User getCurrentUser() throws UserException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UserException("user not found");
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UserException("User not found");
        }
        return user;
    }

    @Override
    public User getUserById(Long id) throws UserException {
        return userRepository.findById(id).orElseThrow(
                ()-> new UserException("user not found")
        );

    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
