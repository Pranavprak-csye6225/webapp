package com.csye6225.webapp.controller;

import com.csye6225.webapp.dto.response.UserResponseDto;
import com.csye6225.webapp.exceptions.UserNotCreatedException;
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
}
