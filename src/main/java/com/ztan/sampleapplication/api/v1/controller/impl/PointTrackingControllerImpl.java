package com.ztan.sampleapplication.api.v1.controller.impl;

import com.ztan.sampleapplication.api.v1.controller.PointTrackingController;
import com.ztan.sampleapplication.api.v1.model.*;
import com.ztan.sampleapplication.api.v1.service.PointTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class PointTrackingControllerImpl implements PointTrackingController {

    private final PointTrackingService pointTrackingService;

    @Autowired
    public PointTrackingControllerImpl(PointTrackingService pointTrackingService) {
        this.pointTrackingService = pointTrackingService;
    }

    @Override
    public ResponseEntity<AddPointsResponse> addPoints(@Valid AddPointsRequest request) {
        AddPointsResponse response =
                new AddPointsResponse(pointTrackingService
                        .addPoints(request.getPayerId(), request.getPoints(), request.getTimeStamp()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GetBalanceResponse> getBalance() {
        GetBalanceResponse response = GetBalanceResponse.builder()
                .records(pointTrackingService.getBalance())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public  ResponseEntity<DeductPointsResponse> deductPoints(@Valid @RequestBody DeductPointsRequest request){
        DeductPointsResponse response = DeductPointsResponse.builder()
                .transactions(pointTrackingService.deductPoints(request.getAmount()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
