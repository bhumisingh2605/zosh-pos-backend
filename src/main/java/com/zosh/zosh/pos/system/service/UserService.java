package com.zosh.zosh.pos.system.service;

import com.zosh.zosh.pos.system.exceptions.UserException;
import com.zosh.zosh.pos.system.modal.User;

import java.util.List;

public interface UserService {

    User getUserFromJwtToken(String token) throws UserException;
    User getCurrentUser() throws UserException;
    User getUserByEmail(String email) throws UserException;
    User getUserById(Long id) throws UserException;
    List<User> getAllUser();



}
