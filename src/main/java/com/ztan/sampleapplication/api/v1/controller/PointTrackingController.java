package com.ztan.sampleapplication.api.v1.controller;

import com.ztan.sampleapplication.api.v1.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RequestMapping("/1/points")
public interface PointTrackingController {
    @PostMapping(produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
    @ResponseBody
    //for the sake of the question, we only have one user.
    //in real world would be addPointsToUser, where taking in a token/ID mapping to the user
    ResponseEntity<AddPointsResponse> addPoints(@Valid @RequestBody AddPointsRequest request);

    @GetMapping(produces = APPLICATION_JSON)
    @ResponseBody
    ResponseEntity<GetBalanceResponse> getBalance(); //in reality get UserId

    @PostMapping(value = "/deduct", produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
    @ResponseBody
    ResponseEntity<DeductPointsResponse> deductPoints(@Valid @RequestBody DeductPointsRequest request);
}
