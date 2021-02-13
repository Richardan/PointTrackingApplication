package com.ztan.sampleapplication.api.v1.model.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRecordDbo {
    String id;
    String payerId;
    Long points;
    Long timeStamp;
}
