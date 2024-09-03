package com.jh.EVSherpa.ui;

import com.jh.EVSherpa.application.StationInfoService;
import com.jh.EVSherpa.application.StationStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/station")
public class AdminStationController {
    private final StationInfoService stationInfoService;
    private final StationStatusService stationStatusService;

    // TODO: IP Blocking

    // save api 강제 호출 (전체 저장, 처음만 사용)
    @PostMapping("/info/save")
    public ResponseEntity<Integer> saveStationInfoFromApi() {
        long start = System.currentTimeMillis();
        int stationInfoSize = stationInfoService.saveStationInfo();
        long end = System.currentTimeMillis();
        log.info("time : {}s", (float)(end-start)/1000);

        return ResponseEntity.ok(stationInfoSize);
    }

    // 추가된 충전소 정보 추가
    @PutMapping("/info/sync")
    public ResponseEntity<Void> sync() {
        long start = System.currentTimeMillis();
        stationInfoService.deleteAndSaveStationInfo();
        long end = System.currentTimeMillis();
        log.info("time : {}s", (float)(end-start)/1000);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // update api 강제 호출
    @PutMapping("/info/update")
    public ResponseEntity<Integer> updateStationInfo() {
        long start = System.currentTimeMillis();
        int updateCount = stationInfoService.updateStationInfo();
        long end = System.currentTimeMillis();
        log.info("time : {}s", (float)(end-start)/1000);

        return ResponseEntity.ok(updateCount);
    }

    // 전체 정보 update 강제 호출
    @PutMapping("/info/update/all")
    public ResponseEntity<Integer> updateStationAllInfo() {
        long start = System.currentTimeMillis();
        int updateCount = stationInfoService.updateStationAllInfo();
        long end = System.currentTimeMillis();
        log.info("time : {}s", (float)(end-start)/1000);
        log.info("updateCount All : {}", updateCount);
        return ResponseEntity.ok(updateCount);
    }

    // StationStatus 강제 update
    @PutMapping("/status/update")
    public ResponseEntity<Integer> updateStationStatus() {
        int updateCount = stationStatusService.updateStationStatus();
        return ResponseEntity.ok(updateCount);
    }

}

