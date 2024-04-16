package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.ChargerInfoApi;
import com.jh.EVSherpa.api.ChargerStatusApi;
import com.jh.EVSherpa.dto.ChargerInfoDto;
import com.jh.EVSherpa.dto.ChargerStatusDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargerApiService {
    private final ChargerStatusApi chargerStatusApi;
    private final ChargerInfoApi chargerInfoApi;

    public int test() {
        List<ChargerStatusDto> chargerStatDtos = chargerStatusApi.callChargerStatusApi();
        System.out.println(chargerStatDtos.size());
        return chargerStatDtos.size();
    }

    public int testInfoApi(){
        List<ChargerInfoDto> chargerInfoDtos = chargerInfoApi.callChargerInfoApi();
        log.info("chargerInfoDtos size : {}", chargerInfoDtos.size());
        return chargerInfoDtos.size();
    }
}
