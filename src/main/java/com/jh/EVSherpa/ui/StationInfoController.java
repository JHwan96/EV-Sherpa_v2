package com.jh.EVSherpa.ui;

import com.jh.EVSherpa.application.StationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
        int stationInfoSize = stationInfoService.saveStationInfo();
        return ResponseEntity.ok(stationInfoSize);
    }

    @PostMapping("/save/test")
    public ResponseEntity<Integer> saveStationInfoFromApiForTest() {
        int stationInfoSize = stationInfoService.saveStationInfoForTest();
        return ResponseEntity.ok(stationInfoSize);
    }

    // update api 강제 호출 (사용자 ㅍ X)
    @PutMapping("/update")
    public ResponseEntity<Integer> updateStationInfo() {
        int updateCount = stationInfoService.updateStationInfo();
        return ResponseEntity.ok(updateCount);
    }

    // update api 강제 호출 (사용자 ㅍ X)
    @PutMapping("/update/test")
    public ResponseEntity<Integer> updateStationInfoForTest() {
        int updateCount = stationInfoService.updateStationInfoForTest();
        return ResponseEntity.ok(updateCount);
    }

    // 전체 정보 update 강제 호출 (사용자 사용 X)
    @PutMapping("/update/all")
    public ResponseEntity<Integer> updateStationAllInfo() {
        int updateCount = stationInfoService.updateStationAllInfo();
        return ResponseEntity.ok(updateCount);
    }
}
