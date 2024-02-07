package com.csye6225.webapp.serviceImpl;

import com.csye6225.webapp.dto.request.UserRequestDto;
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
    public UserResponseDto createdUser(User user) throws UserNotCreatedException {
        Optional<User> requestedUser = userRepository.findByUsername(user.getUsername());
        if (requestedUser.isPresent())
            throw new UserNotCreatedException("Username already exists");
        try {
            user.setPassword(bcryptEncoder(user.getPassword()));
            User createdUser = userRepository.save(user);
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
    public void updateUser(UserRequestDto user, String authorization) throws UserNotUpdatedException, UserNotFoundException {
        User userDb = getUserFromDb(authorization);
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

    public User getUserFromDb(String authorization) throws UserNotFoundException{
        String[] usernamePassword = base64Decoder(authorization);
        Optional<User> requestedUser = userRepository.findByUsername(usernamePassword[0]);
        System.out.println(requestedUser);
        if (requestedUser.isEmpty())
            throw new UserNotFoundException("User Not Found");
        else if (passwordCheck(usernamePassword[1], requestedUser.get().getPassword())) {
            return requestedUser.get();
        } else{
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
