package com.ztan.sampleapplication.api.v1.service;

import com.ztan.sampleapplication.api.v1.model.AddPointsResponse;
import com.ztan.sampleapplication.api.v1.model.Transaction;

import java.util.List;

public interface PointTrackingService {
    Long addPoints(String payerId, Long points, String timeStamp);

    List<String> getBalance();

    List<Transaction> deductPoints(Long amount);
}
