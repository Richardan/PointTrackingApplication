package com.ztan.sampleapplication.dao.impl;

import com.ztan.sampleapplication.api.v1.model.dbo.BalanceDbo;
import com.ztan.sampleapplication.api.v1.model.dbo.UserRecordDbo;
import com.ztan.sampleapplication.dao.MockDataSource;
import com.ztan.sampleapplication.dao.UserPointsDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemUserPointsDaoImpl implements UserPointsDao {
    public static final String COMMA = ",";
    public static final String SPACE = " ";
    public static final String POINTS = "points";

    @Override
    public Long addPoints(UserRecordDbo recordDbo) {
        //Mock repo.save()
        if(recordDbo.getPoints() > 0) {
            MockDataSource.pointsRecordTbl.offer(recordDbo);
            return getTotalBalance();
        } else {
            //Really sh*tty code here to mock how a real DB works
            return mockAddNegativePoints(recordDbo);
        }
    }

    @Override
    public void updateBalance(BalanceDbo balanceDbo) {
        //Mocking: Update xxx value(yyy, payerId, points+old balance);
        //in real SQL, could done with trigger
        log.info("Updating: {} with {}", MockDataSource.userBalanceTbl, balanceDbo);
        MockDataSource.userBalanceTbl.put(balanceDbo.getPayerId(),
                MockDataSource.userBalanceTbl.getOrDefault(balanceDbo.getPayerId(), 0L)+balanceDbo.getPoints());
    }

    @Override
    public Long getTotalBalance() {
        //Mocking: select * and aggregate
        //Might be better just keep a row in table called total
        return MockDataSource.userBalanceTbl.values().stream()
                .collect(Collectors.summingLong(Long::longValue));
    }

    @Override
    public Long getPayerBalance(String payerId){
        return MockDataSource.userBalanceTbl.getOrDefault(payerId, 0L);
    }

    @Override
    public UserRecordDbo getNextPointsRecord() {
        UserRecordDbo record = MockDataSource.pointsRecordTbl.poll();
        return record;
    }

    private Long mockAddNegativePoints(UserRecordDbo recordDbo){
        if(recordDbo.getPoints() >= 0){
            log.error("InMemUserPointsDaoImpl:mockAddNegativePoints error.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error!");
        }
        List<UserRecordDbo> toPutBack = new ArrayList<>();
        boolean jobDone = false;
        while(!jobDone) {
            UserRecordDbo next = MockDataSource.pointsRecordTbl.poll();
            if(next == null){
                toPutBack.add(recordDbo);
                jobDone = true;
            }
            else if(!next.getPayerId().equals(recordDbo.getPayerId())){
                toPutBack.add(next);
            } else if(next.getPoints()<0){
                next.setPoints(next.getPoints()+recordDbo.getPoints());
                toPutBack.add(next);
                jobDone = true;
            } else if(next.getPoints() < Math.abs(recordDbo.getPoints())) {
                recordDbo.setPoints(recordDbo.getPoints()+next.getPoints());
            } else if(next.getPoints() > Math.abs(recordDbo.getPoints())){
                next.setPoints(next.getPoints()+recordDbo.getPoints());
                toPutBack.add(next);
                jobDone = true;
            } else {
                // next.getPoints() == recordDbo.getPoints()
                // both discard
                jobDone = true;
            }
        }
        for(UserRecordDbo dbo : toPutBack) {
            MockDataSource.pointsRecordTbl.offer(dbo);
        }
        printContent(MockDataSource.pointsRecordTbl);
        return getTotalBalance();
    }

    public List<String> getDetailedBalance() {
        List<String> results = new LinkedList<>();
        MockDataSource.userBalanceTbl.entrySet().forEach(entry -> {
            results.add(recordBuilder(entry.getKey(), entry.getValue()));
        });
        return results;
    }

    private String recordBuilder(String payerId, Long balance) {
        StringBuilder builder = new StringBuilder();
        builder.append(payerId).append(COMMA).append(SPACE)
                .append(balance).append(SPACE).append(POINTS);
        return builder.toString();
    }

    private void printContent(PriorityQueue<UserRecordDbo> queue) {
        for(UserRecordDbo dbo : new ArrayList<>(queue)){
            log.info("-->" + dbo.toString());
        }
    }
}
