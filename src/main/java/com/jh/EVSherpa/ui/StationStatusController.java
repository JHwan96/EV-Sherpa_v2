package com.jh.EVSherpa.ui;

import com.jh.EVSherpa.application.StationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/station/status")
public class StationStatusController {
    private final StationStatusService stationStatusService;


}
