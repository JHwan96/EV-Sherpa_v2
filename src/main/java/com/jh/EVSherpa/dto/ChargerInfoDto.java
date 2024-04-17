package com.jh.EVSherpa.dto;

import com.jh.EVSherpa.dto.enums.ChargerMethod;
import com.jh.EVSherpa.dto.enums.ChargerStatus;
import com.jh.EVSherpa.dto.enums.ChargerType;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChargerInfoDto {
    private String stationName;     // 충전소명
    private String stationChargerId;       // 충전소+충전기 Id
    private ChargerType chargerType;     // 충전기타입
    private String address;         // 주소
    private String location;        // 상세위치
    private Point position;         // 경,위도
    private String useTime;         // 이용 가능 시간
    private String businessId;      // 기관 아이디
    private String bName;           // 기관명
    private String businessName;    // 운영기관명
    private String businessCall;    // 운영기관 연락처
    private ChargerStatus stat;            // 충전기 상태
    private LocalDateTime stationUpdateDt;  // 상태 갱신 일시
    private LocalDateTime lastTsDt;         // 마지막 충전시작일시
    private LocalDateTime lastTeDt;         // 마지막 충전종료일시
    private LocalDateTime nowTsDt;          // 충전중 시작일시
    private int output;         // 충전 용량
    private ChargerMethod chargerMethod;      // 충전방식 (단독/동시)
    private String zcode;       // 지역 코드
    private String zscode;      // 지역구분 상세코드
    private String kind;        // 충전소 구분 코드
    private String kindDetail;  // 충전소 구분 상세코드
    private String parkingFree;  // 주차료무료
    private String note;        // 충전소 안내
    private String limitYn;     // 이용자 제한
    private String limitDetail; // 이용제한 사유
    private String delYn;       // 삭제 여부
    private String delDetail;       // 삭제 사유
    private String trafficYn;       // 편의제공 여부
}
