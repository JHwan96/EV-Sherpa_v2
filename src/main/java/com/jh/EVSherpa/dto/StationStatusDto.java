package com.jh.EVSherpa.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StationStatusDto {
    private String businessId;
    private String stationChargerId;       // 충전소+충전기 Id
    private String stat;
    private LocalDateTime statUpdDt;
    private LocalDateTime lastTsdt;
    private LocalDateTime lastTedt;
    private LocalDateTime nowTsdt;
}
