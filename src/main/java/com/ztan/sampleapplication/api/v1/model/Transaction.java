package com.ztan.sampleapplication.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Transaction {
    private String payerId;

    private Long points;

    private String timeStamp;
}
