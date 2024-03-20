package com.csye6225.webapp.controller;

import com.csye6225.webapp.dto.request.CreateUserRequestDto;
import com.csye6225.webapp.dto.request.UpdateUserRequestDto;
import com.csye6225.webapp.dto.response.UserResponseDto;
import com.csye6225.webapp.exceptions.UserNotCreatedException;
import com.csye6225.webapp.exceptions.UserNotFoundException;
import com.csye6225.webapp.exceptions.UserNotUpdatedException;
import com.csye6225.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final HttpHeaders headers;

    @Autowired
    public UserController(UserService userService) {
        logger.info("In User Controller");
        this.userService = userService;
        this.headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponseDto> createUser(@RequestParam Map<String, String> queryParameter, @RequestBody CreateUserRequestDto user, @RequestHeader(value = "authorization", required = false) String authorization) throws UserNotCreatedException {
        logger.info("In POST user method");
        if (null != queryParameter && !queryParameter.isEmpty()) {
            logger.error("Query parameter is given: "+queryParameter);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        } else if (null != authorization) {
            logger.error("Authorization is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        try {
            UserResponseDto createdUserDto = userService.createdUser(user);
            logger.info("POST Request Success and newly created user: "+ createdUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).headers(this.headers).body(createdUserDto);
        } catch (UserNotCreatedException uex) {
            throw uex;
        } catch (Exception e) {
            throw new UserNotCreatedException("User not created");
        }
    }

    @GetMapping("/self")
    public ResponseEntity<UserResponseDto> getUser(@RequestParam Map<String, String> queryParameter, @RequestBody(required = false) String payload, @RequestHeader("authorization") String authorization) throws UserNotFoundException {
        logger.info("In GET user method");
        logger.debug("The request given by the user: "+ payload);
        if (null != payload && !payload.isEmpty()) {
            logger.error("Payload is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        } else if (null != queryParameter && !queryParameter.isEmpty()) {
            logger.error("Query parameter is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        UserResponseDto userDto = userService.getUser(authorization);
        logger.info("GET Request Success and user: "+ userDto);
        return ResponseEntity.status(HttpStatus.OK).headers(this.headers).body(userDto);
    }

    @PutMapping(path = "/self", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> updateUser(@RequestParam Map<String, String> queryParameter, @RequestBody UpdateUserRequestDto user, @RequestHeader("authorization") String authorization) throws UserNotUpdatedException, UserNotFoundException {
        logger.info("In PUT user method");
        logger.debug("The request given by the user: "+ user);
        if (null != queryParameter && !queryParameter.isEmpty()) {
            logger.error("Query parameter is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(this.headers).build();
        }
        try {
            userService.updateUser(user, authorization);
            logger.info("PUT request is success");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).headers(this.headers).build();
        } catch (UserNotUpdatedException | UserNotFoundException uex) {
            throw uex;
        } catch (Exception e) {
            throw new UserNotUpdatedException("User not updated");
        }
    }

    @RequestMapping(method = {RequestMethod.HEAD, RequestMethod.OPTIONS}, path = {"","/self"})
    public ResponseEntity<Void> handleHeadOptionsCall() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
        logger.error("Wrong HTTP Method Given");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).build();
    }


}
