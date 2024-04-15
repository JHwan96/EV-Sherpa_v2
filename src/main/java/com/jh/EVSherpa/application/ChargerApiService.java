package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.ChargerStatusApi;
import com.jh.EVSherpa.dto.ChargerStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargerApiService {
    private final ChargerStatusApi chargerStatusApi;

    public int test() {
        List<ChargerStatusDto> chargerStatDtos = chargerStatusApi.callChargerStatusApi();
        System.out.println(chargerStatDtos.size());
        return chargerStatDtos.size();
    }
}
