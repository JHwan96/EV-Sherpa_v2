package com.jh.EVSherpa.domain;

import com.jh.EVSherpa.dto.enums.ChargerMethod;
import com.jh.EVSherpa.dto.enums.ChargerType;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "charger_info_id")
    private Long id;

    private String stationName;
    @Column(nullable = false)
    private String stationChargerId;
    @Enumerated(value = EnumType.STRING)
    private ChargerType chargerType;
    private String address;
    private String location;
    private Point point;
    private String useTime;
    private String businessId;
    private String businessName;           // 기관명
    private String operatorName;    // 운영기관명
    private String operatorCall;    // 운영기관 연락처
    private int output;         // 충전 용량
    @Enumerated(value = EnumType.STRING)
    private ChargerMethod chargerMethod;      // 충전방식 (단독/동시)
    private String zcode;       // 지역 코드
    private String zscode;      // 지역구분 상세코드
    private String kind;        // 충전소 구분 코드
    private String kindDetail;  // 충전소 구분 상세코드
    private String parkingFree;  // 주차료무료
    private String notation;        // 충전소 안내
    private String limitYn;     // 이용자 제한
    private String limitDetail; // 이용제한 사유
    private String deleteYn;       // 삭제 여부
    private String deleteDetail;       // 삭제 사유
    private String trafficYn;       // 편의제공 여부

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_status_id")
    private StationStatus stationStatus;
}
