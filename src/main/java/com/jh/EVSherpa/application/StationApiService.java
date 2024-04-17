package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.StationInfoApi;
import com.jh.EVSherpa.api.StationStatusApi;
import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationApiService {
    private final StationStatusApi chargerStatusApi;
    private final StationInfoApi chargerInfoApi;

    public int test() {
        List<StationStatusDto> chargerStatDtos = chargerStatusApi.callChargerStatusApi();
        System.out.println(chargerStatDtos.size());
        return chargerStatDtos.size();
    }

    public int testInfoApi(){
        List<StationInfoDto> chargerInfoDtos = chargerInfoApi.callStationInfoApi();
        log.info("chargerInfoDtos size : {}", chargerInfoDtos.size());
        return chargerInfoDtos.size();
    }
}
