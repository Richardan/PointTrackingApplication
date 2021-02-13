package com.ztan.sampleapplication.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPointsRequest {
    @NotNull
    private String payerId;

    @NotNull
    private Long points;

    @NotNull
    private String timeStamp;
}
