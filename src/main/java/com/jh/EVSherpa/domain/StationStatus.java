package com.jh.EVSherpa.domain;

import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.dto.StationStatusUpdateDto;
import com.jh.EVSherpa.dto.enums.ChargerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@SequenceGenerator(
        name="STATUS_SEQ_GENERATOR",
        sequenceName = "STATUS_SEQ",
        allocationSize=100
)
public class StationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUS_SEQ_GENERATOR")
    private Long id;

    @Column(nullable = false)
    private String stationChargerId;

    @Enumerated(value = EnumType.STRING)
    private ChargerStatus status;            // 충전기 상태
    private LocalDateTime stationUpdateDate;  // 상태 갱신 일시
    private LocalDateTime lastChargeStart;         // 마지막 충전시작일시
    private LocalDateTime lastChargeEnd;         // 마지막 충전종료일시
    private LocalDateTime nowChargeStart;          // 충전중 시작일시

    @OneToOne(mappedBy = "stationStatus", fetch = FetchType.LAZY)
    private StationInfo stationInfo;

    public void updateStatus(StationStatusUpdateDto request) {
        this.status = request.getStatus();
        this.stationUpdateDate = request.getStationUpdateDate();
        this.lastChargeStart = request.getLastChargeStart();
        this.lastChargeEnd = request.getLastChargeEnd();
        this.nowChargeStart = request.getNowChargeStart();
    }

    public static StationStatus fromInfoDto(StationInfoDto dto){
        return StationStatus.builder()
                .status(dto.getStatus())
                .stationChargerId(dto.getStationChargerId())
                .stationUpdateDate(dto.getStationUpdateDate())
                .lastChargeStart(dto.getLastChargeStart())
                .lastChargeEnd(dto.getLastChargeEnd())
                .nowChargeStart(dto.getNowChargeStart())
                .build();
    }

    public static StationStatus fromStatusDto(StationStatusDto dto){
        return StationStatus.builder()
                .stationChargerId(dto.getStationChargerId())
                .status(dto.getStatus())
                .stationUpdateDate(dto.getStationUpdateDate())
                .lastChargeStart(dto.getLastChargeStart())
                .lastChargeEnd(dto.getLastChargeEnd())
                .nowChargeStart(dto.getNowChargeStart())
                .build();
    }
}
