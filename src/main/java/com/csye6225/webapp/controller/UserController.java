package com.csye6225.webapp.controller;

import com.csye6225.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
