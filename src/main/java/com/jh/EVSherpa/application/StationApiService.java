package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.StationInfoApi;
import com.jh.EVSherpa.api.StationStatusApi;
import com.jh.EVSherpa.domain.StationInfo;
import com.jh.EVSherpa.domain.StationStatus;
import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.repository.StationInfoRepository;
import com.jh.EVSherpa.repository.StationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationApiService {
    private final StationStatusApi stationStatusApi;
    private final StationInfoApi stationInfoApi;
    private final StationInfoRepository stationInfoRepository;
    private final StationStatusRepository stationStatusRepository;

    //StationStatus 호출 및 저장 메서드 (처음 저장할 때만 사용)
    public List<StationStatus> saveStationStatus() {
        // api 호출 - StationStatus
        List<StationStatusDto> stationStatusDtos = stationStatusApi.callStationStatusApi();
        // 데이터 저장
        return stationStatusRepository.saveAll(stationStatusDtos);
    }

    // StationInfoApi 호출 및 저장 메서드 (처음 저장할 때만 사용)
    public int saveStationInfo() {
        // api 호출 - StationInfo
        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApi();
        // 전체 저장 실행
        List<StationInfo> stationInfos = stationInfoRepository.saveAll(stationInfoDtos);
        return stationInfos.size();
    }

//    // 충전소 정보 갱신을 위한 update 메서드
//    public int updateStationInfo() {
//        // api 호출 - StationInfo
//        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApi();
//
//    }

}
