package com.ztan.sampleapplication.api.v1.service.impl;

import com.ztan.sampleapplication.api.v1.model.Transaction;
import com.ztan.sampleapplication.api.v1.model.dbo.BalanceDbo;
import com.ztan.sampleapplication.api.v1.model.dbo.UserRecordDbo;
import com.ztan.sampleapplication.api.v1.service.PointTrackingService;
import com.ztan.sampleapplication.dao.UserPointsDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
public class PointTrackingServiceImpl implements PointTrackingService {
    private final UserPointsDao userPointsDao;

    @Autowired
    public PointTrackingServiceImpl(UserPointsDao userPointsDao) {
        this.userPointsDao = userPointsDao;
    }

    @Override
    public Long addPoints(String payerId, Long points, String timeStamp) {
        //assuming points cannot go negative for each payer
        if(points < 0 && Math.abs(points) > userPointsDao.getPayerBalance(payerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enough balance for this payer");
        }

        UserRecordDbo userRecordDbo = new UserRecordDbo(mockGetUUId(), payerId, points, parseTimeStamp(timeStamp));
        // transaction or in real SQL, could done with trigger
        userPointsDao.updateBalance(new BalanceDbo(payerId, points));
        return userPointsDao.addPoints(userRecordDbo);
    }

    @Override
    public List<String> getBalance() {
        return userPointsDao.getDetailedBalance();
    }

    @Override
    public List<Transaction> deductPoints(Long amount){
        if(userPointsDao.getTotalBalance() < amount) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enough balance!");
        }
        Map<String, Long> transactionMap = new LinkedHashMap<>(); //just in case we want result with order as well
        List<Transaction> newTransactions = new LinkedList<>();

        while(amount > 0) {
            //keep polling from priority queue to fulfill deduction <=> use points on chronologically order
            UserRecordDbo recordDbo = userPointsDao.getNextPointsRecord();
            if(recordDbo.getPoints() >= amount) {
                transactionMap.put(recordDbo.getPayerId(),
                        transactionMap.getOrDefault(recordDbo.getPayerId(), 0L) - amount);
                if(recordDbo.getPoints()-amount > 0) {
                    recordDbo.setPoints(recordDbo.getPoints() - amount);
                    userPointsDao.addPoints(recordDbo);
                }
                amount = 0L;
            } else if(recordDbo.getPoints() < amount) {
                transactionMap.put(recordDbo.getPayerId(),
                        transactionMap.getOrDefault(recordDbo.getPayerId(), 0L) - recordDbo.getPoints());
                amount -= recordDbo.getPoints();
            }
        }

        transactionMap.forEach((key, value) -> {
            newTransactions.add(new Transaction(key, value, "now"));
            userPointsDao.updateBalance(new BalanceDbo(key, value));
        });
        return newTransactions;
    }

    private Long parseTimeStamp(String input) {
        //10/31 10AM
        if(input.equalsIgnoreCase("now")) return System.currentTimeMillis();
        String dateString = input + " " + Calendar.getInstance().get(Calendar.YEAR);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd hha yyyy", Locale.ENGLISH);
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            //todo get dynamic timezone
            return dateTime.atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli();
        } catch (DateTimeParseException exception){
            log.warn("Parsing exception:", exception);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Date format ");
        }
    }

    private String mockGetUUId() {
        Random random = new Random();
        return String.valueOf(random.nextLong());
    }
}
