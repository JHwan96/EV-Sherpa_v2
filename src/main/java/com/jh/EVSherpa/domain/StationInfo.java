package com.jh.EVSherpa.domain;

import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationInfoUpdateDto;
import com.jh.EVSherpa.dto.enums.ChargerMethod;
import com.jh.EVSherpa.dto.enums.ChargerType;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SequenceGenerator(
        name="INFO_SEQ_GENERATOR",
        sequenceName = "INFO_SEQ",
        initialValue = 1, allocationSize=100
)
public class StationInfo {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="INFO_SEQ_GENERATOR")
    @Column(name = "station_info_id")
    private Long id;

    private String stationName;
    @Column(nullable = false, unique = true)
    private String stationChargerId;
    @Enumerated(value = EnumType.STRING)
    private ChargerType chargerType;
    private String address;
    private String location;

    @Column(name="pointer", columnDefinition = "GEOMETRY")
    private Point pointer;
    private String useTime;
    private String businessId;
    private String businessName;    // 기관명
    private String operatorName;    // 운영기관명
    private String operatorCall;    // 운영기관 연락처
    private Integer output;         // 충전 용량 (50 이상 급속)
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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "station_status_id")
    private StationStatus stationStatus;


    public void updateStationStatus(StationStatus stationStatus){
        this.stationStatus = stationStatus;
    }

    public void updateStationInfo(StationInfoUpdateDto request){
        this.chargerType = request.getChargerType();
        this.useTime = request.getUseTime();
        this.operatorName = request.getOperatorName();
        this.operatorCall = request.getOperatorCall();
        this.output = request.getOutput();
        this.parkingFree = request.getParkingFree();
        this.notation = request.getNotation();
        this.limitYn = request.getLimitYn();
        this.limitDetail = request.getLimitDetail();
    }

    public static StationInfo fromDto(StationInfoDto dto, StationStatus stationStatus) {
        return StationInfo.builder()
                .stationName(dto.getStationName())
                .stationChargerId(dto.getStationChargerId())
                .chargerType(dto.getChargerType())
                .address(dto.getAddress())
                .location(dto.getLocation())
                .pointer(dto.getPosition())
                .useTime(dto.getUseTime())
                .businessId(dto.getBusinessId())
                .businessName(dto.getBusinessName())
                .operatorCall(dto.getOperatorCall())
                .operatorName(dto.getOperatorName())
                .output(dto.getOutput())
                .chargerMethod(dto.getChargerMethod())
                .zcode(dto.getZcode())
                .zscode(dto.getZscode())
                .kind(dto.getKind())
                .kindDetail(dto.getKindDetail())
                .parkingFree(dto.getParkingFree())
                .notation(dto.getNotation())
                .limitYn(dto.getLimitYn())
                .limitDetail(dto.getLimitDetail())
                .deleteYn(dto.getDeleteYn())
                .deleteDetail(dto.getDeleteDetail())
                .trafficYn(dto.getTrafficYn())
                .stationStatus(stationStatus)
                .build();
    }
}
