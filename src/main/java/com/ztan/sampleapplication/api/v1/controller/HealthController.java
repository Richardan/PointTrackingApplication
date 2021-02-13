package com.ztan.sampleapplication.api.v1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthController {
    @GetMapping("/health_check")
    @ResponseBody
    public ResponseEntity healthCheck() {
        log.debug("Health checked!");
        return new ResponseEntity(HttpStatus.OK);
    }
}
