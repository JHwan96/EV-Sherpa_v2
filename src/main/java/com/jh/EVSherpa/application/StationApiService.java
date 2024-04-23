package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.StationInfoApi;
import com.jh.EVSherpa.api.StationStatusApi;
import com.jh.EVSherpa.domain.StationInfo;
import com.jh.EVSherpa.domain.StationStatus;
import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationInfoUpdateDto;
import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.repository.StationInfoRepository;
import com.jh.EVSherpa.repository.StationStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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
        long start = System.currentTimeMillis();
        List<StationInfo> stationInfos = stationInfoRepository.saveAll(stationInfoDtos);
        long end = System.currentTimeMillis();
        log.info("stationInfoRepository.saveAll : {}s", (float) (end - start) / 1000);

        return stationInfos.size();
    }

    // 충전소 정보 갱신을 위한 update 메서드
    public List<StationInfo> updateStationInfoJpql() {
        // api 호출 - StationInfo
        List<StationInfoUpdateDto> stationInfoUpdateDtos = stationInfoApi.callStationInfoApiForUpdate();
        // stationInfo 정보 갱신
        return stationInfoRepository.updateAllByJpql(stationInfoUpdateDtos);
    }

    public List<StationInfo> updateStationInfoDirtyCheck() {
        // api 호출 - StationInfo
        List<StationInfoUpdateDto> stationInfoUpdateDtos = stationInfoApi.callStationInfoApiForUpdate();
        // stationInfo 정보 갱신
        return stationInfoRepository.updateAllByDirtyChecking(stationInfoUpdateDtos);
    }

}
