package com.ztan.sampleapplication.dao;

import com.ztan.sampleapplication.api.v1.model.dbo.UserRecordDbo;
import lombok.Data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@Data
public class MockDataSource {
    // Table userBalanceTbl mocking
    // |payerId|balance|
    public static final Map<String, Long> userBalanceTbl = new HashMap<>();

    // Table pointsRecordTbl mocking
    //|ID|Payer_ID|Points|timestamp
    public static final PriorityQueue<UserRecordDbo> pointsRecordTbl =
            new PriorityQueue<>(Comparator.comparingLong(UserRecordDbo::getTimeStamp));
}
