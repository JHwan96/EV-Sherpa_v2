package com.jh.EVSherpa.repository;

import com.jh.EVSherpa.domain.StationStatus;
import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StationStatusRepository {
    private final EntityManager em;

    public StationStatus save(Long id) {
        return em.find(StationStatus.class, id);
    }

    //전체 저장 메서드 (테스트용)
    public List<StationStatus> saveAll(List<StationStatusDto> requests) {
        List<StationStatus> statuses = new ArrayList<>();       // TODO: 나중에 필요없을지 확인
        for (StationStatusDto request : requests) {
            StationStatus stationStatus = StationStatus.fromStatusDto(request);
            em.persist(stationStatus);
            statuses.add(stationStatus);
        }
        em.flush();
        em.clear();

        return statuses;
    }

    public Optional<StationStatus> findById(Long id) {
        return Optional.ofNullable(em.find(StationStatus.class, id));
    }

    public List<StationStatus> findAll() {
        return em.createQuery("SELECT ss FROM StationStatus ss", StationStatus.class)
                .getResultList();
    }

    public StationStatus updateById(StationStatusDto request, Long id) {
        StationStatus stationStatus = Optional.of(em.find(StationStatus.class, id))
                .orElseThrow(() -> new NotFoundException("충전소 상태정보가 없습니다."));
        stationStatus.updateStatus(request);
        return stationStatus;
    }

    public int updateAll(List<StationStatusDto> requests) {
        int count = 0;
        String jpql = "UPDATE StationStatus ss SET " +
                "ss.status = :status, " +
                "ss.stationUpdateDate = :stationUpdateDate, " +
                "ss.lastChargeStart = :lastChargeStart, " +
                "ss.lastChargeEnd = :lastChargeEnd, " +
                "ss.nowChargeStart = :nowChargeStart " +
                "WHERE ss.stationChargerId = :stationChargerId";
        for (StationStatusDto request : requests) {
            int i = em.createQuery(jpql)
                    .setParameter("status", request.getStatus())
                    .setParameter("stationUpdateDate", request.getStationUpdateDate())
                    .setParameter("lastChargeStart", request.getLastChargeStart())
                    .setParameter("lastChargeEnd", request.getLastChargeEnd())
                    .setParameter("nowChargerStart", request.getNowChargeStart())
                    .setParameter("stationChargerId", request.getStationChargerId())
                    .executeUpdate();
            count += i;
        }
        log.info("update count : {}", count);
        return count;
    }
}
