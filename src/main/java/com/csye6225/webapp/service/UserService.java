package com.csye6225.webapp.service;

import com.csye6225.webapp.dto.request.CreateUserRequestDto;
import com.csye6225.webapp.dto.request.UpdateUserRequestDto;
import com.csye6225.webapp.dto.response.UserResponseDto;
import com.csye6225.webapp.exceptions.UserNotCreatedException;
import com.csye6225.webapp.exceptions.UserNotFoundException;
import com.csye6225.webapp.exceptions.UserNotUpdatedException;
import com.csye6225.webapp.exceptions.UserNotVerifiedException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService {
    UserResponseDto createdUser(CreateUserRequestDto user) throws UserNotCreatedException;
    UserResponseDto getUser(String authorization) throws UserNotFoundException, UserNotVerifiedException;
    void updateUser(UpdateUserRequestDto user, String authorization) throws UserNotUpdatedException, UserNotFoundException, UserNotVerifiedException;
    String[] base64Decoder(String token);
    String bcryptEncoder(String password);
    boolean passwordCheck(String rawPassword, String hashedPassword);
    String verifyUser(Map<String, String> queryParameter, String isIntegrationTest) throws UserNotVerifiedException;
}
