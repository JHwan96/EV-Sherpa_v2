package com.jh.EVSherpa.repository;

import com.jh.EVSherpa.domain.StationStatus;
import com.jh.EVSherpa.dto.StationStatusDto;
import com.jh.EVSherpa.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

    //TODO: 삭제해야할지 고민
    public void save(StationStatusDto request) {
        Optional<StationStatus> findStationStatus = findByStationChargerId(request.getStationChargerId());
        StationStatus stationStatus = StationStatus.fromStatusDto(request);

        if(findStationStatus.isEmpty()){
            em.persist(stationStatus);
        } else {
            StationStatus find = findStationStatus.get();
            find.updateStatus(request);
        }
    }

    //전체 저장 메서드 (테스트용)
    //TODO: 필요없을 수 있음
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

    // stationChargerId로 찾는 메서드
    public Optional<StationStatus> findByStationChargerId(String stationChargerId) {
        try {
            StationStatus stationStatus = em.createQuery("SELECT ss FROM StationStatus ss WHERE ss.stationChargerId = :stationChargerId", StationStatus.class)
                    .setParameter("stationChargerId", stationChargerId)
                    .getSingleResult();
            return Optional.of(stationStatus);
        } catch(NoResultException e){
            return Optional.empty();
        }

    }

    // 전체 충전소 상태정보 반환
    public List<StationStatus> findAll() {
        return em.createQuery("SELECT ss FROM StationStatus ss", StationStatus.class)
                .getResultList();
    }

    //TODO: 삭제해야할지 고민
    public StationStatus updateById(StationStatusDto request, Long id) {
        StationStatus stationStatus = Optional.of(em.find(StationStatus.class, id))
                .orElseThrow(() -> new NotFoundException("충전소 상태정보가 없습니다."));
        stationStatus.updateStatus(request);
        return stationStatus;
    }

    // 전체 update 메서드
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
