package com.csye6225.webapp.serviceImpl;

import com.csye6225.webapp.dto.request.CreateUserRequestDto;
import com.csye6225.webapp.dto.request.UpdateUserRequestDto;
import com.csye6225.webapp.dto.response.UserResponseDto;
import com.csye6225.webapp.exceptions.UserNotCreatedException;
import com.csye6225.webapp.exceptions.UserNotFoundException;
import com.csye6225.webapp.exceptions.UserNotUpdatedException;
import com.csye6225.webapp.model.User;
import com.csye6225.webapp.repository.UserRepository;
import com.csye6225.webapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserResponseDto createdUser(CreateUserRequestDto user) throws UserNotCreatedException {
        Optional<User> requestedUser = userRepository.findByUsername(user.getUsername());
        if (requestedUser.isPresent())
            throw new UserNotCreatedException("Username already exists");
        try {
            if (null != user.getPassword() && !user.getPassword().isBlank()) {
                user.setPassword(bcryptEncoder(user.getPassword()));
            }
            User createdUser = userRepository.save(this.modelMapper.map(user, User.class));
            return this.modelMapper.map(createdUser, UserResponseDto.class);
        } catch (Exception e) {
            System.out.println("CreateUserEx: " + e);
            throw new UserNotCreatedException("User cannot be created");
        }
    }

    @Override
    public UserResponseDto getUser(String authorization) throws UserNotFoundException {
        return this.modelMapper.map(getUserFromDb(authorization), UserResponseDto.class);
    }

    @Override
    public void updateUser(UpdateUserRequestDto user, String authorization) throws UserNotUpdatedException, UserNotFoundException {
        User userDb = getUserFromDb(authorization);
        if (null == user.getFirstName() && null == user.getLastName() && null == user.getPassword()) {
            throw new UserNotUpdatedException("All fields are null or empty");
        } else if ((null != user.getPassword() && user.getPassword().isBlank()) || (null != user.getFirstName() && user.getFirstName().isBlank()) || (null != user.getLastName() && user.getLastName().isBlank())) {
            throw new UserNotUpdatedException("Empty value is given in some fields");
        }
        try {
            if (null != user.getPassword()) {
                userDb.setPassword(bcryptEncoder(user.getPassword()));
            }
            if (null != user.getFirstName()) {
                userDb.setFirstName(user.getFirstName());
            }
            if (null != user.getLastName()) {
                userDb.setLastName(user.getLastName());
            }
            userRepository.save(userDb);
        } catch (Exception e) {
            throw new UserNotUpdatedException("User not updated");
        }

    }

    public User getUserFromDb(String authorization) throws UserNotFoundException {
        String[] usernamePassword = base64Decoder(authorization);
        if (null == usernamePassword || usernamePassword.length < 2) {
            throw new UserNotFoundException("Username or password wrong");
        }
        Optional<User> requestedUser = userRepository.findByUsername(usernamePassword[0]);
        System.out.println(requestedUser);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("User Not Found");
        else if (passwordCheck(usernamePassword[1], requestedUser.get().getPassword())) {
            return requestedUser.get();
        } else {
            throw new UserNotFoundException("Invalid Password");
        }
    }

    @Override
    public String[] base64Decoder(String token) {
        String baseToken = token.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(baseToken);
        String decodedString = new String(decodedBytes);
        return decodedString.split(":");
    }


    @Override
    public String bcryptEncoder(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Override
    public boolean passwordCheck(String rawPassword, String hashedPassword) {
        BCryptPasswordEncoder checker = new BCryptPasswordEncoder();
        return checker.matches(rawPassword, hashedPassword);
    }
}
