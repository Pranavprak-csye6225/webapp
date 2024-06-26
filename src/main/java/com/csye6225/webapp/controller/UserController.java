package com.csye6225.webapp.controller;

import com.csye6225.webapp.dto.request.CreateUserRequestDto;
import com.csye6225.webapp.dto.request.UpdateUserRequestDto;
import com.csye6225.webapp.dto.response.UserResponseDto;
import com.csye6225.webapp.exceptions.UserNotCreatedException;
import com.csye6225.webapp.exceptions.UserNotFoundException;
import com.csye6225.webapp.exceptions.UserNotUpdatedException;
import com.csye6225.webapp.exceptions.UserNotVerifiedException;
import com.csye6225.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/v8/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

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
    public ResponseEntity<UserResponseDto> createUser(@RequestParam Map<String, String> queryParameter, @RequestBody CreateUserRequestDto user, @RequestHeader(value = "authorization", required = false) String authorization) throws UserNotCreatedException {
        logger.info("In POST user method for user: " + user.getUsername());
        if (null != queryParameter && !queryParameter.isEmpty()) {
            logger.error("Query parameter is given: " + queryParameter + ", by user" + user.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        } else if (null != authorization) {
            logger.error("Authorization is given, by user" + user.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        try {
            UserResponseDto createdUserDto = userService.createdUser(user);
            logger.info("POST Request Success and newly created user: " + createdUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).headers(this.headers).body(createdUserDto);
        } catch (UserNotCreatedException uex) {
            logger.error("Exception occurred for user while creating: " + user.getUsername());
            throw uex;
        } catch (Exception e) {
            logger.error("Exception occurred for user while creating: " + user.getUsername());
            throw new UserNotCreatedException("User not created");
        }
    }

    @GetMapping("/self")
    public ResponseEntity<UserResponseDto> getUser(@RequestParam Map<String, String> queryParameter, @RequestBody(required = false) String payload, @RequestHeader("authorization") String authorization) throws UserNotFoundException, UserNotVerifiedException {
        logger.debug("The request given by the user for get call: " + payload);
        if (null != payload && !payload.isEmpty()) {
            logger.error("Payload is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        } else if (null != queryParameter && !queryParameter.isEmpty()) {
            logger.error("Query parameter is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        UserResponseDto userDto = userService.getUser(authorization);
        logger.info("GET Request Success and user: " + userDto);
        return ResponseEntity.status(HttpStatus.OK).headers(this.headers).body(userDto);
    }

    @PutMapping(path = "/self", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> updateUser(@RequestParam Map<String, String> queryParameter, @RequestBody UpdateUserRequestDto user, @RequestHeader("authorization") String authorization) throws UserNotUpdatedException, UserNotFoundException, UserNotVerifiedException {
        logger.debug("The request given by the user: " + user);
        if (null != queryParameter && !queryParameter.isEmpty()) {
            logger.error("Query parameter is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        try {
            userService.updateUser(user, authorization);
            logger.info("PUT request is success for user:" + user.getFirstName());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(this.headers).build();
        } catch (UserNotUpdatedException | UserNotFoundException | UserNotVerifiedException uex) {
            logger.error("Exception occurred for user while creating: " + user.getFirstName());
            throw uex;
        } catch (Exception e) {
            logger.error("Exception occurred for user while creating: " + user.getFirstName());
            throw new UserNotUpdatedException("User not updated");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verifyUser(@RequestParam Map<String, String> queryParameter, @RequestBody(required = false) String payload, @RequestHeader(value = "isIntegrationTest", required = false) String isIntegrationTest) throws UserNotVerifiedException {
        logger.debug("The request given by the user: " + queryParameter);
        if (null != payload && !payload.isEmpty()) {
            logger.error("Payload is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        String message = userService.verifyUser(queryParameter, isIntegrationTest);
        logger.info("VerificationStatus for user:" + message + ", using" + queryParameter);
        return ResponseEntity.status(HttpStatus.OK).headers(this.headers).body(Collections.singletonMap("verificationStatus", message));
    }

    @RequestMapping(method = {RequestMethod.HEAD, RequestMethod.OPTIONS}, path = {"", "/self"})
    public ResponseEntity<Void> handleHeadOptionsCall() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
        logger.error("Wrong HTTP Method Given");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).build();
    }


}
