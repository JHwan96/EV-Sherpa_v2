package com.jh.EVSherpa.ui;

import com.jh.EVSherpa.application.StationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/station/info")
public class StationInfoController {    // TODO: IP 제한
    private final StationInfoService stationInfoService;

    // save api 강제 호출 (사용자 사용 X)
    @PostMapping("/save")
    public ResponseEntity<Integer> saveStationInfoFromApi() {
        long start = System.currentTimeMillis();
        int stationInfoSize = stationInfoService.saveStationInfo();
        long end = System.currentTimeMillis();
        log.info("time : {}s", (float)(end-start)/1000);

        return ResponseEntity.ok(stationInfoSize);
    }

    @GetMapping("/test")
    public ResponseEntity<Integer> test() throws InterruptedException {
        long start = System.currentTimeMillis();
        stationInfoService.addNewStationInfo();
        long end = System.currentTimeMillis();
        log.info("time : {}s", (float)(end-start)/1000);
        return ResponseEntity.ok(1);
    }

    @GetMapping("/find/all")
    public ResponseEntity<Integer> findAllStationInfo() {
        int allStationInfo = stationInfoService.findAllStationInfo();
        return ResponseEntity.ok(allStationInfo);
    }

    // update api 강제 호출 (사용자 ㅍ X)
    @PutMapping("/update")
    public ResponseEntity<Integer> updateStationInfo() {
        long start = System.currentTimeMillis();
        int updateCount = stationInfoService.updateStationInfo();
        long end = System.currentTimeMillis();
        log.info("time : {}s", (float)(end-start)/1000);

        return ResponseEntity.ok(updateCount);
    }

    // 전체 정보 update 강제 호출 (사용자 사용 X)
    @PutMapping("/update/all")
    public ResponseEntity<Integer> updateStationAllInfo() {
        long start = System.currentTimeMillis();
        int updateCount = stationInfoService.updateStationAllInfo();
        long end = System.currentTimeMillis();
        log.info("time : {}s", (float)(end-start)/1000);
        log.info("updateCount All : {}", updateCount);
        return ResponseEntity.ok(updateCount);
    }

    // 한 페이지만 저장 (9999개 테스트용)
    //TODO: 삭제해야함
    @PostMapping("/save/test")
    public ResponseEntity<Integer> saveStationInfoFromApiForTest() {
        int stationInfoSize = stationInfoService.saveStationInfoForPageTest();
        return ResponseEntity.ok(stationInfoSize);
    }

    // update api 강제 호출 (사용자 ㅍ X)
    @PutMapping("/update/test")
    public ResponseEntity<Integer> updateStationInfoForTest() {
        int updateCount = stationInfoService.updateStationInfoForPageTest();
        return ResponseEntity.ok(updateCount);
    }


}
