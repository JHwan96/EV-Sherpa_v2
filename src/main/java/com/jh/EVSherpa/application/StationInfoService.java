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

        // 충전소 정보 9999개 저장 테스트용)
        public int saveStationInfoForPageTest() {
        // api 호출 - StationInfo
        long start = System.currentTimeMillis();
        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApiForTest();
        log.info("callStationInfoApiForTest: {}s", (float)(System.currentTimeMillis()-start)/1000);
        log.info("count: {}", stationInfoDtos.size());

        return stationInfoRepository.saveAll(stationInfoDtos);
    }

    // 충전소 N개만 갱신 (테스트용)
    public int updateStationInfoForPageTest() {
        // api 호출 - StationInfo
        long start = System.currentTimeMillis();
        List<StationInfoUpdateDto> stationInfoUpdateDtos = stationInfoApi.callStationInfoApiForUpdateTest();
        log.info("callStationInfoApiForTest: {}s", (float)(System.currentTimeMillis()-start)/1000);
        return stationInfoRepository.updateAll(stationInfoUpdateDtos);
    }

    // StationInfoApi 호출 및 저장 메서드 (사용자 사용 X)
    public int saveStationInfo() {
        // api 호출 - StationInfo
        int totalCount = stationInfoApi.callApiForTotalCount();
        List<List<StationInfoDto>> stationInfoDtos = stationInfoApi.callAllStationInfoApi(totalCount);
        log.info("count: {}", stationInfoDtos.size());
        // 전체 저장 실행
        return stationInfoRepository.saveAllList(stationInfoDtos);
    }

    // 충전소 정보 update 메서드  (사용자 사용 X)
    public int updateStationInfo() {
        // api 호출 - StationInfo
        int totalCount = stationInfoApi.callApiForTotalCount();
        List<List<StationInfoUpdateDto>> stationInfoUpdateDtos = stationInfoApi.callStationInfoApiForUpdate(totalCount);
        log.info("{}",stationInfoUpdateDtos.size());
        // stationInfo 정보 갱신
        return stationInfoRepository.updateAllList(stationInfoUpdateDtos);
    }


    // 충전소 정보 전체 update 메서드  (사용자 사용 X)
    public int updateStationAllInfo() {
        int totalCount = stationInfoApi.callApiForTotalCount();
        List<List<StationInfoDto>> stationInfoDtos = stationInfoApi.callAllStationInfoApi(totalCount);
        List<StationInfoDto> tempStationInfoDtos = stationInfoDtos.get(0);
        return stationInfoRepository.updateAllInfo(tempStationInfoDtos);
    }
}
