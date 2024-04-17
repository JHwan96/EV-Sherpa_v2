package com.jh.EVSherpa.domain;

import com.jh.EVSherpa.dto.enums.ChargerMethod;
import com.jh.EVSherpa.dto.enums.ChargerStatus;
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
public class ChargerInfo {
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
    private String bName;           // 기관명
    private String businessName;    // 운영기관명
    private String businessCall;    // 운영기관 연락처
    private int output;         // 충전 용량
    @Enumerated(value = EnumType.STRING)
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stationChargerId")
    private ChargerStatus chargerStatus;
}
