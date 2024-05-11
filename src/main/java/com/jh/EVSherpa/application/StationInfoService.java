package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.StationInfoApi;
import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationInfoUpdateDto;
import com.jh.EVSherpa.repository.StationInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StationInfoService {
    private final StationInfoApi stationInfoApi;
    private final StationInfoRepository stationInfoRepository;

    // 충전소 정보 9999개 저장 테스트용)
    // TODO: 삭제 해야함
    public int saveStationInfoForPageTest() {
        // api 호출 - StationInfo
        long start = System.currentTimeMillis();
        List<StationInfoDto> stationInfoDtos = stationInfoApi.callStationInfoApiForTest();
        log.info("callStationInfoApiForTest: {}s", (float) (System.currentTimeMillis() - start) / 1000);
        log.info("count: {}", stationInfoDtos.size());

        return stationInfoRepository.saveAll(stationInfoDtos);
    }

    // 충전소 정보 9999개 update 테스트용)
    // TODO: 삭제 해야함
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

    // 충전소 일부 정보 update 메서드  (사용자 사용 X)
    //TODO: scheduling, 삭제 여부 확인
    public int updateStationInfo() {
        // api 호출 - StationInfo
        int totalCount = stationInfoApi.callApiForTotalCount();
        List<List<StationInfoUpdateDto>> stationInfoUpdateDtos = stationInfoApi.callStationInfoApiForUpdate(totalCount);
        log.info("{}", stationInfoUpdateDtos.size());
        // stationInfo 정보 갱신
        return stationInfoRepository.updateAllList(stationInfoUpdateDtos);
    }

    //테스트용 findAll
    //TODO: 사용에 따라 변경해야함
    @Transactional(readOnly = true)
    public int findAllStationInfo() {
        List<String> all = stationInfoRepository.findAllStationChargerId();
        return all.size();
    }

    // DB 내의 충전소 정보 전체 update 메서드  (사용자 사용 X)
    // 3시간에 한번씩
    public int updateStationAllInfo() {
        int totalCount = stationInfoApi.callApiForTotalCount();
        List<List<StationInfoDto>> stationInfoDtos = stationInfoApi.callAllStationInfoApi(totalCount);
        return stationInfoRepository.updateAllInfo(stationInfoDtos);
    }


    //전체 동기화 메서드
    //하루 2번
    public void deleteAndSaveStationInfo() {
        int totalCount = stationInfoApi.callApiForTotalCount(); // 전체 개수 반환
        log.info("total size:{}", totalCount);

        Set<String> stationChargerIdSetFromRepo = new HashSet<>(stationInfoRepository.findAllStationChargerId());
        List<List<StationInfoDto>> stationInfoDtoList = stationInfoApi.callAllStationInfoApi(totalCount);
        List<StationInfoDto> stationInfoDtos = removeNestList(stationInfoDtoList);

        // 새로 추가된 전기차 충전소만 확인
        saveNewStationInfo(stationChargerIdSetFromRepo, stationInfoDtos);

        // DB에 있지만, API 호출에는 없는 것 (철거된 것들)
        deleteRemovedStationInfo(stationChargerIdSetFromRepo, stationInfoDtos);
    }

    // 새로 추가된 StationInfo 저장 메서드
    private void saveNewStationInfo(Set<String> setFromRepo, List<StationInfoDto> stationInfoDtoList) {
        int saveCount = 0;
        Set<String> setFromApi = getStationChargerIdSetFromDtoList(stationInfoDtoList);
        Set<String> missingSet = diffFromFirstParam(setFromApi, setFromRepo);
        if (!missingSet.isEmpty()) {
            List<StationInfoDto> needToSaveDtoList = stationInfoDtoFromMissingSet(stationInfoDtoList, missingSet);
            log.info("needToSaveDtoList : {}", needToSaveDtoList.size());
            saveCount = stationInfoRepository.saveAll(needToSaveDtoList);
        }
        log.info("saveAll : {}", saveCount);
    }

    private static List<StationInfoDto> stationInfoDtoFromMissingSet(List<StationInfoDto> stationInfoDtoList, Set<String> missingSet) {
        return stationInfoDtoList.stream()
                .filter(x -> missingSet.contains(x.getStationChargerId()))
                .toList();
    }

    private void deleteRemovedStationInfo(Set<String> setFromRepo, List<StationInfoDto> stationInfoDtos) {
        int deleteCount = 0;
        Set<String> setFromApi = getStationChargerIdSetFromDtoList(stationInfoDtos);

        //DB엔 있고, API 호출에는 없는 StationChargerId
        Set<String> deleteSet = diffFromFirstParam(setFromRepo, setFromApi);
        log.info("deleteSet : {}", deleteSet.size());

        if (!deleteSet.isEmpty()) {
            deleteCount = stationInfoRepository.deleteByAllStationChargerId(deleteSet);
        }
        log.info("deleteByAllStationChargerId : {}", deleteCount);
    }

    /**
     * 중첩 리스트 한겹을 없애는 메서드
     */
    @NotNull
    private static List<StationInfoDto> removeNestList(List<List<StationInfoDto>> stationInfoDtoList) {
        return stationInfoDtoList.stream().flatMap(List::stream).toList();
    }

    //api에 없는 것만 Set으로 (차집합)
    private static Set<String> diffFromFirstParam(Set<String> setFromApi, Set<String> setFromRepo) {
        return setFromApi.stream()
                .filter(x -> !setFromRepo.contains(x))
                .collect(Collectors.toSet());
    }

    /**
     * StationInfoDto 리스트에서 stationChargerId만 추출해서 Set으로 반환
     */
    @NotNull
    private static Set<String> getStationChargerIdSetFromDtoList(List<StationInfoDto> stationInfoDtoList) {
        return stationInfoDtoList.stream()
                .map(StationInfoDto::getStationChargerId)
                .collect(Collectors.toSet());
    }

}
