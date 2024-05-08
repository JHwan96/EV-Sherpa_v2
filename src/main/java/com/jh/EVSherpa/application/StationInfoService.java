package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.StationInfoApi;
import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationInfoUpdateDto;
import com.jh.EVSherpa.repository.StationInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        log.info("callStationInfoApiForTest: {}s", (float) (System.currentTimeMillis() - start) / 1000);
        log.info("count: {}", stationInfoDtos.size());

        return stationInfoRepository.saveAll(stationInfoDtos);
    }

    // 충전소 N개만 갱신 (테스트용)
    public int updateStationInfoForPageTest() {
        // api 호출 - StationInfo
        long start = System.currentTimeMillis();
        List<StationInfoUpdateDto> stationInfoUpdateDtos = stationInfoApi.callStationInfoApiForUpdateTest();
        log.info("callStationInfoApiForTest: {}s", (float) (System.currentTimeMillis() - start) / 1000);
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
        log.info("{}", stationInfoUpdateDtos.size());
        // stationInfo 정보 갱신
        return stationInfoRepository.updateAllList(stationInfoUpdateDtos);
    }


    // 충전소 정보 전체 update 메서드  (사용자 사용 X)
    public int updateStationAllInfo() {
        int totalCount = stationInfoApi.callApiForTotalCount();
        List<List<StationInfoDto>> stationInfoDtos = stationInfoApi.callAllStationInfoApi(totalCount);
        return stationInfoRepository.updateAllInfo(stationInfoDtos);
    }

    //테스트
    public void addNewStationInfo() {
        int totalCount = stationInfoApi.callApiForTotalCount(); // 전체 개수 반환
        List<StationInfoDto> saveStationInfoDto = new ArrayList<>();
        List<String> stationChargerIdFromRepo = stationInfoRepository.findAllStationChargerId();

        List<List<StationInfoDto>> stationInfoDtoList = stationInfoApi.callAllStationInfoApi(totalCount);//TODO: List<String> 반환하는 것을 따로만들지 고민
        log.info("callAllStationInfoApi Done");

        List<String> stationChargerIdFromApi = stationInfoDtoList.stream()
                .flatMap(List::stream)
                .map(StationInfoDto::getStationChargerId)
                .toList();
        log.info("stream 1번 Done");

        List<String> missingList = stationChargerIdFromApi.stream().filter(x -> !stationChargerIdFromRepo.contains(x)).toList();
        log.info("stream 2번 Done");
        log.info("missingList : {}", missingList.size());
        //TODO: 비교 후 saveAll
    }

    //테스트용 findAll
    public int findAllStationInfo() {
        List<String> all = stationInfoRepository.findAllStationChargerId();
        return all.size();
    }
}
