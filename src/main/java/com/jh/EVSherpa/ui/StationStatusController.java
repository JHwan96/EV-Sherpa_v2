package com.jh.EVSherpa.ui;

import com.jh.EVSherpa.application.StationStatusService;
import com.jh.EVSherpa.domain.StationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/station/status")
public class StationStatusController {
    private final StationStatusService stationStatusService;

    @PostMapping("/save")
    public ResponseEntity<List<StationStatus>> saveStationStatus() { //TODO: 실제로 반환할거면 DTO로 변경
        List<StationStatus> stationStatuses = stationStatusService.saveStationStatus();
        return ResponseEntity.ok(stationStatuses);
    }

    @PutMapping("/update")
    public ResponseEntity<Integer> updateStationStatus() {
        int updateCount = stationStatusService.updateStationStatus();
        return ResponseEntity.ok(updateCount);
    }

}
