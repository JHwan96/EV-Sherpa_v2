package com.jh.EVSherpa.domain;

import com.jh.EVSherpa.dto.enums.ChargerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class StationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String stationChargerId;

    @Enumerated(value= EnumType.STRING)
    private ChargerStatus stat;            // 충전기 상태
    private LocalDateTime stationUpdateDate;  // 상태 갱신 일시
    private LocalDateTime lastChargeStart;         // 마지막 충전시작일시
    private LocalDateTime lastChargeEnd;         // 마지막 충전종료일시
    private LocalDateTime nowChargeStart;          // 충전중 시작일시

    @OneToOne(mappedBy = "stationStatus", fetch = FetchType.LAZY)
    private StationInfo stationInfo;
}
