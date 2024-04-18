package com.jh.EVSherpa.dto;

import com.jh.EVSherpa.dto.enums.ChargerStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StationStatusUpdateDto {
    private ChargerStatus status;
    private LocalDateTime stationUpdateDate;
    private LocalDateTime lastChargeStart;
    private LocalDateTime lastChargeEnd;
    private LocalDateTime nowChargeStart;
}
