package com.ztan.sampleapplication.api.v1.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetBalanceResponse {
    List<String> records;
}
