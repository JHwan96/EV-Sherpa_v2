package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.StationInfoApi;
import com.jh.EVSherpa.domain.StationInfo;
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
        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApi();
        // 전체 저장 실행
        List<StationInfo> stationInfos = stationInfoRepository.saveAll(stationInfoDtos);

        return stationInfos.size();
    }

    // 충전소 정보 update 메서드  (사용자 사용 X)
    public int updateStationInfo() {
        // api 호출 - StationInfo
        List<StationInfoUpdateDto> stationInfoUpdateDtos = stationInfoApi.callStationInfoApiForUpdate();
        // stationInfo 정보 갱신
        return stationInfoRepository.updateAll(stationInfoUpdateDtos);
    }

    // 충전소 정보 전체 update 메서드  (사용자 사용 X)
    public int updateStationAllInfo() {
        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApi();
        return stationInfoRepository.updateAllInfo(stationInfoDtos);
    }
}
