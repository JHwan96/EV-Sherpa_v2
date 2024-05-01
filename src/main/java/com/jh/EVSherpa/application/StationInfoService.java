package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.StationInfoApi;
import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationInfoUpdateDto;
import com.jh.EVSherpa.repository.StationInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StationInfoService {
    private final StationInfoApi stationInfoApi;
    private final StationInfoRepository stationInfoRepository;

    // StationInfoApi 호출 및 저장 메서드 (사용자 사용 X)
    public int saveStationInfo() {
        // api 호출 - StationInfo
        List<List<StationInfoDto>> stationInfoDtos = stationInfoApi.callStationInfoApi();
        log.info("count: {}", stationInfoDtos.size());
        // 전체 저장 실행
        return stationInfoRepository.saveAllList(stationInfoDtos);
    }

    public int saveStationInfoForTest() {
        // api 호출 - StationInfo
        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApiForTest();
        log.info("count: {}", stationInfoDtos.size());
        // 전체 저장 실행
        return stationInfoRepository.saveAll(stationInfoDtos);
    }

    // 충전소 정보 update 메서드  (사용자 사용 X)
    public int updateStationInfo() {
        // api 호출 - StationInfo
        List<List<StationInfoUpdateDto>> stationInfoUpdateDtos = stationInfoApi.callStationInfoApiForUpdate();
        log.info("{}",stationInfoUpdateDtos.size());
        // stationInfo 정보 갱신
        return stationInfoRepository.updateAllList(stationInfoUpdateDtos);
    }

    // 충전소 정보 update 메서드  (사용자 사용 X)
    public int updateStationInfoForTest() {
        // api 호출 - StationInfo
        List<StationInfoUpdateDto> stationInfoUpdateDtos = stationInfoApi.callStationInfoApiForUpdateForTest();
        // stationInfo 정보 갱신
        return stationInfoRepository.updateAll(stationInfoUpdateDtos);
    }

    // 충전소 정보 전체 update 메서드  (사용자 사용 X)
    public int updateStationAllInfo() {
        List<List<StationInfoDto>> stationInfoDtos = stationInfoApi.callStationInfoApi();
        List<StationInfoDto> tempStationInfoDtos = stationInfoDtos.get(0);
        return stationInfoRepository.updateAllInfo(tempStationInfoDtos);
    }
}
