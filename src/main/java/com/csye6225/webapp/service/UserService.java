package com.csye6225.webapp.service;

import com.csye6225.webapp.dto.response.UserResponseDto;
import com.csye6225.webapp.exceptions.UserNotCreatedException;
import com.csye6225.webapp.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDto createdUser(User user) throws UserNotCreatedException;
    String[] base64Decoder(String token);
    String bcryptEncoder(String password);
    boolean passwordCheck(String rawPassword, String hashedPassword);
}
