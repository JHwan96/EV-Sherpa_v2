package com.jh.EVSherpa.dto;

import com.jh.EVSherpa.dto.enums.ChargerStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StationStatusDto {
    private String businessId;
    private String stationChargerId;       // 충전소+충전기 Id
    private ChargerStatus status;
    private LocalDateTime stationUpdateDate;
    private LocalDateTime lastChargeStart;
    private LocalDateTime lastChargeEnd;
    private LocalDateTime nowChargeStart;
}
