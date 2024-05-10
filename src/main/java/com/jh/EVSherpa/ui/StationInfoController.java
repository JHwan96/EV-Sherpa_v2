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

    @GetMapping("/find/all")
    public ResponseEntity<Integer> findAllStationInfo() {
        int allStationInfo = stationInfoService.findAllStationInfo();
        return ResponseEntity.ok(allStationInfo);
    }

    // 한 페이지만 저장 (9999개 테스트용)
    //TODO: 삭제해야함
    @PostMapping("/save/test")
    public ResponseEntity<Integer> saveStationInfoFromApiForTest() {
        int stationInfoSize = stationInfoService.saveStationInfoForPageTest();
        return ResponseEntity.ok(stationInfoSize);
    }

    // update api 강제 호출 (9999개 테스트용)
    //TODO: 삭제해야함
    @PutMapping("/update/test")
    public ResponseEntity<Integer> updateStationInfoForTest() {
        int updateCount = stationInfoService.updateStationInfoForPageTest();
        return ResponseEntity.ok(updateCount);
    }


}
