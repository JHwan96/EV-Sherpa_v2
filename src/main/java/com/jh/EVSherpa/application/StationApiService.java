package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.StationInfoApi;
import com.jh.EVSherpa.api.StationStatusApi;
import com.jh.EVSherpa.domain.StationInfo;
import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.repository.StationInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationApiService {
    private final StationStatusApi stationStatusApi;
    private final StationInfoApi stationInfoApi;
    private final StationInfoRepository stationInfoRepository;

    public int test() {
        List<StationStatusDto> stationStatusDtos = stationStatusApi.callChargerStatusApi();

        System.out.println(stationStatusDtos.size());
        return stationStatusDtos.size();
    }

    public int testInfoApi() {
        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApi();

        log.info("chargerInfoDtos size : {}", stationInfoDtos.size());
        return stationInfoDtos.size();
    }

    // api 호출 및 repository 저장 메서드
    public List<StationInfo> saveStationInfo() {
        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApi();
        return stationInfoRepository.saveAll(stationInfoDtos);
    }
}
