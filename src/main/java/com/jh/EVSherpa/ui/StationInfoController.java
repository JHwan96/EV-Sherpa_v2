package com.jh.EVSherpa.ui;

import com.jh.EVSherpa.application.StationApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/station/info")
public class StationInfoController {
    private final StationApiService stationApiService;

    // save api 강제 호출
    @PostMapping("/save")
    public ResponseEntity<Integer> saveStationInfoFromApi() {
        int stationInfoSize = stationApiService.saveStationInfo();
        return ResponseEntity.ok(stationInfoSize);
    }

    // update api 강제 호출
    @PostMapping("/update")
    public ResponseEntity<Integer> updateStationInfo() {
        int updateCount = stationApiService.updateStationInfo();
        return ResponseEntity.ok(updateCount);
    }
}
