package com.jh.EVSherpa.application;

import com.jh.EVSherpa.api.StationStatusApi;
import com.jh.EVSherpa.domain.StationStatus;
import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.repository.StationStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StationStatusService {
    private final StationStatusApi stationStatusApi;
    private final StationStatusRepository stationStatusRepository;

    //StationStatus 호출 및 저장 메서드 (처음 저장할 때만 사용)
    public List<StationStatus> saveStationStatus() {
        // api 호출 - StationStatus
        List<StationStatusDto> stationStatusDtos = stationStatusApi.callStationStatusApi();
        // 데이터 저장
        return stationStatusRepository.saveAll(stationStatusDtos);
    }

    //StationStatus update 메서드
    public int updateStationStatus() {
        // api 호출
        List<StationStatusDto> stationStatusDtos = stationStatusApi.callStationStatusApi();
        // 데이터 update
        return stationStatusRepository.updateAll(stationStatusDtos);
    }
}
