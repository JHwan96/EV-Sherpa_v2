package com.jh.EVSherpa.application.scheduler;

import com.jh.EVSherpa.application.StationStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
@EnableScheduling
public class StationStatusScheduler {
    private final StationStatusService stationStatusService;

    /**
     * 충전기 상태 정보 갱신
     * *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
     * 초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     */
    @Scheduled(cron = "* */20 * * * *")
    public void updateStationStatus(){
        log.info("updateStationStatus task cron jobs");
        stationStatusService.updateStationStatus();
    }
}
