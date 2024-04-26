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
public class StationInfoController {
    private final StationInfoService stationInfoService;

    // save api 강제 호출
    @PostMapping("/save")
    public ResponseEntity<Integer> saveStationInfoFromApi() {
        int stationInfoSize = stationInfoService.saveStationInfo();
        return ResponseEntity.ok(stationInfoSize);
    }

    // update api 강제 호출
    @PutMapping("/update")
    public ResponseEntity<Integer> updateStationInfo() {
        int updateCount = stationInfoService.updateStationInfo();
        return ResponseEntity.ok(updateCount);
    }
}
