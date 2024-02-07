package com.csye6225.webapp.service;

import com.csye6225.webapp.dto.request.UserRequestDto;
import com.csye6225.webapp.dto.response.UserResponseDto;
import com.csye6225.webapp.exceptions.UserNotCreatedException;
import com.csye6225.webapp.exceptions.UserNotFoundException;
import com.csye6225.webapp.exceptions.UserNotUpdatedException;
import com.csye6225.webapp.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponseDto createdUser(User user) throws UserNotCreatedException;
    UserResponseDto getUser(String authorization) throws UserNotFoundException;
    void updateUser(UserRequestDto user, String authorization) throws UserNotUpdatedException, UserNotFoundException;
    String[] base64Decoder(String token);
    String bcryptEncoder(String password);
    boolean passwordCheck(String rawPassword, String hashedPassword);
}
