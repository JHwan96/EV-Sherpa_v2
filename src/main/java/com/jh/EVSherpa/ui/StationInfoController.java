package com.jh.EVSherpa.ui;

import com.jh.EVSherpa.application.StationApiService;
import com.jh.EVSherpa.domain.StationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/station/info")
public class StationInfoController {
    private final StationApiService stationApiService;

    @PostMapping("/save")
    public ResponseEntity<Integer> saveStationInfoFromApi() {
        int i = stationApiService.saveStationInfo();
        return ResponseEntity.ok(i);
    }
    @PostMapping("/update")
    public ResponseEntity<List<StationInfo>> updateStationInfo() {
        List<StationInfo> stationInfos = stationApiService.updateStationInfoCheck();
        return ResponseEntity.ok(stationInfos);
    }

}
