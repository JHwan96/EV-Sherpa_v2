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
    private final StationStatusRepository stationStatusRepository;

    public int test() {
        List<StationStatusDto> stationStatusDtos = stationStatusApi.callStationStatusApi();

        System.out.println(stationStatusDtos.size());
        return stationStatusDtos.size();
    }

    /**
     * StationStatus 호출 및 저장 메서드 (처음 저장할 때만 사용)
     * @return StationStatus의 리스트
     */
    public List<StationStatus> saveStationStatus(){
        log.info("saveStationStatus start");

        // api 호출 - StationStatus
        log.info("StationStatusApi start");
        long start = System.currentTimeMillis();
        List<StationStatusDto> stationStatusDtos = stationStatusApi.callStationStatusApi();
        long end = System.currentTimeMillis();
        log.info("API 호출 시간 : {}s", (float)(end-start)/1000);

        log.info("데이터 저장 start");
        long saveStart = System.currentTimeMillis();
        List<StationStatus> result = stationStatusRepository.saveAll(stationStatusDtos);
        long saveEnd = System.currentTimeMillis();
        log.info("데이터 저장시간 : {}s", (saveEnd-saveStart)/1000);

        return result;
    }

    /**
     * StationInfoApi 호출 및 저장 메서드 (처음 저장할 때만 사용)
     * @return 저장된 갯수 (차후 변경)
     */
    public int saveStationInfo() {
        log.info("saveStationInfo() start");

        // api 호출 - StationInfo
        List<StationInfoDto> stationInfoDtos = callStationInfo();

        log.info("데이터 저장 start");
        long start = System.currentTimeMillis();

        // 전체 저장 실행
        List<StationInfo> stationInfos = stationInfoRepository.saveAll(stationInfoDtos);
        long end = System.currentTimeMillis();
        log.info("데이터 저장 시간 : {}s", (float) (end - start) / 1000);

        return stationInfos.size();
    }

    private List<StationInfoDto> callStationInfo(){
        log.info("callStationInfo start");      //TODO: 차후 제거
        long start = System.currentTimeMillis();

        // api 호출 - StationInfo
        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApi();

        long end = System.currentTimeMillis();
        log.info("API 호출 시간 : {}s", (float)(end-start)/1000);
        return stationInfoDtos;
    }
}
