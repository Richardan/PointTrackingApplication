package com.ztan.sampleapplication.dao;

import com.ztan.sampleapplication.api.v1.model.dbo.BalanceDbo;
import com.ztan.sampleapplication.api.v1.model.dbo.UserRecordDbo;

import java.util.List;

public interface UserPointsDao {
    Long addPoints(UserRecordDbo userRecordDbo);

    void updateBalance(BalanceDbo balanceDbo);

    List<String> getDetailedBalance();

    Long getTotalBalance();

    Long getPayerBalance(String payerId);

    UserRecordDbo getNextPointsRecord();
}
