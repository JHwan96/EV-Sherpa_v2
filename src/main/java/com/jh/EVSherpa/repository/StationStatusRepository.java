package com.jh.EVSherpa.repository;

import com.jh.EVSherpa.domain.StationStatus;
import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.dto.StationStatusUpdateDto;
import com.jh.EVSherpa.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StationStatusRepository {
    private final EntityManager em;

    public StationStatus save(Long id) {
        return em.find(StationStatus.class, id);
    }

    //전체 저장 메서드
    public List<StationStatus> saveAll(List<StationStatusDto> requests){
        List<StationStatus> statuses = new ArrayList<>();       // TODO: 나중에 필요없을지 확인
        for(StationStatusDto request : requests) {
            StationStatus stationStatus = StationStatus.fromStatusDto(request);
            em.persist(stationStatus);
            statuses.add(stationStatus);
        }
        return statuses;
    }



    public Optional<StationStatus> findById(Long id) {
        return Optional.ofNullable(em.find(StationStatus.class, id));
    }

    public List<StationStatus> findAll() {
        return em.createQuery("SELECT ss FROM StationStatus ss", StationStatus.class)
                .getResultList();
    }

    public StationStatus updateById(StationStatusUpdateDto request, Long id) {
        StationStatus stationStatus = Optional.of(em.find(StationStatus.class, id))
                .orElseThrow(() -> new NotFoundException("충전소 상태정보가 없습니다."));
        stationStatus.updateStatus(request);
        return stationStatus;
    }
}
