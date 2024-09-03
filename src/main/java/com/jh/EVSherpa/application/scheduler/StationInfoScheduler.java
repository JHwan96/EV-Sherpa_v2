package com.jh.EVSherpa.application.scheduler;

import com.jh.EVSherpa.application.StationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@EnableScheduling
@Slf4j
public class StationInfoScheduler {
    private final StationInfoService stationInfoService;

    /**
     *
     * *           *　　　　　　*　　　　　　*　　　　　　*　　　　　　*
     * 초(0-59)   분(0-59)　　시간(0-23)　　일(1-31)　　월(1-12)　　요일(0-7)
     */
    @Scheduled(cron = "0 30 */4 * * *")
    @Async
    public void updateAllStationInfo(){
        log.info("updateAllStationInfo task cron jobs");
        stationInfoService.updateStationAllInfo();
    }

    @Scheduled(cron = "0 30 3,16 * * *")
    @Async
    public void syncStationInfo(){
        log.info("syncStationInfo task cron jobs");
        stationInfoService.deleteAndSaveStationInfo();
    }

}
