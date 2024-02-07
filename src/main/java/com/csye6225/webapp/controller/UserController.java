package com.csye6225.webapp.controller;

import com.csye6225.webapp.dto.request.UserRequestDto;
import com.csye6225.webapp.dto.response.UserResponseDto;
import com.csye6225.webapp.exceptions.UserNotCreatedException;
import com.csye6225.webapp.exceptions.UserNotFoundException;
import com.csye6225.webapp.exceptions.UserNotUpdatedException;
import com.csye6225.webapp.model.User;
import com.csye6225.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;
    private final HttpHeaders headers;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponseDto> createUser(@RequestParam Map<String, String> queryParameter, @RequestBody User user) throws UserNotCreatedException {
        if (null != queryParameter && !queryParameter.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        try {
            UserResponseDto createdUserDto = userService.createdUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).headers(this.headers).body(createdUserDto);
        } catch (UserNotCreatedException uex) {
            throw uex;
        } catch (Exception e) {
            throw new UserNotCreatedException("User not created");
        }
    }

    @GetMapping("/self")
    public ResponseEntity<UserResponseDto> getUser(@RequestParam Map<String, String> queryParameter, @RequestBody(required = false) String payload, @RequestHeader("authorization") String authorization) throws UserNotFoundException {
        if (null != payload && !payload.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        } else if (null != queryParameter && !queryParameter.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        try {
            UserResponseDto userDto = userService.getUser(authorization);
            return ResponseEntity.status(HttpStatus.OK).headers(this.headers).body(userDto);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    @PutMapping(path = "/self", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> updateUser(@RequestParam Map<String, String> queryParameter, @RequestBody UserRequestDto user, @RequestHeader("authorization") String authorization) throws UserNotUpdatedException, UserNotFoundException {
        if (null != queryParameter && !queryParameter.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        try {
            userService.updateUser(user, authorization);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(this.headers).build();
        } catch (UserNotUpdatedException | UserNotFoundException uex) {
            throw uex;
        } catch (Exception e) {
            throw new UserNotUpdatedException("User not updated");
        }
    }

}
