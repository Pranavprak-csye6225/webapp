package com.csye6225.webapp.controller;

import com.csye6225.webapp.service.HealthCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/healthz")
public class HealthCheckController {
    Logger logger = LoggerFactory.getLogger(HealthCheckController.class);
    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping
    public ResponseEntity<Void> getHealthCheck(@RequestParam Map<String, String> queryParameter, @RequestBody(required = false) String payload, @RequestHeader(value = "authorization", required = false) String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        logger.info("Getting Health check");
        headers.setPragma("no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
        if (null != authorization) {
            logger.error("Authorization is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        } else if (null != payload && !payload.isEmpty()) {
            logger.error("Payload is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        } else if (null != queryParameter && !queryParameter.isEmpty()) {
            logger.error("Query Parameter is given");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).build();
        } else if (healthCheckService.isDatabaseConnected()) {
            return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).headers(headers).build();
        }
    }

    @RequestMapping(method = {RequestMethod.HEAD, RequestMethod.OPTIONS})
    public ResponseEntity<Void> handleHeadOptionsCall() {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().mustRevalidate());
        headers.setPragma("no-cache");
        headers.add("X-Content-Type-Options", "nosniff");
        logger.error("Wrong HTTP Method is given");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).build();
    }
}
