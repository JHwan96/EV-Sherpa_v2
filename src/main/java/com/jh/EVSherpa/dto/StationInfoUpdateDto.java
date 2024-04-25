package com.jh.EVSherpa.dto;

import com.jh.EVSherpa.dto.enums.ChargerType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StationInfoUpdateDto {
    // 충전소명
    private String stationChargerId;       // 충전소+충전기 Id
    private ChargerType chargerType;     // 충전기타입
    private String useTime;         // 이용 가능 시간
    private String operatorName;    // 운영기관명
    private String operatorCall;    // 운영기관 연락처
    private Integer output;         // 충전 용량
    private LocalDateTime stationUpdateDate;
    private String parkingFree;  // 주차료무료
    private String notation;        // 충전소 안내
    private String limitYn;     // 이용자 제한
    private String limitDetail; // 이용제한 사유
    private String deleteYn;       // 삭제 여부
    private String deleteDetail;       // 삭제 사유
    private String trafficYn;       // 편의제공 여부
}
