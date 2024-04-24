package com.jh.EVSherpa.repository;

import com.jh.EVSherpa.domain.StationInfo;
import com.jh.EVSherpa.domain.StationStatus;
import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.dto.StationInfoUpdateDto;
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
public class StationInfoRepository {
    private final EntityManager em;

    public StationInfo save(StationInfoDto request) {
        StationStatus stationStatus = StationStatus.fromInfoDto(request);
        StationInfo stationInfo = StationInfo.fromDto(request, stationStatus);
        em.persist(stationInfo);
        return stationInfo;
    }

    //전체 저장 메서드
    public List<StationInfo> saveAll(List<StationInfoDto> requests) {
        List<StationInfo> stationInfos = new ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            StationStatus stationStatus = StationStatus.fromInfoDto(requests.get(i));
            StationInfo stationInfo = StationInfo.fromDto(requests.get(i), stationStatus);
            em.persist(stationInfo);
            stationInfos.add(stationInfo);
        }
        em.flush();
        em.clear();
        return stationInfos;
    }

    public Optional<StationInfo> findById(Long id) {
        StationInfo stationInfo = em.find(StationInfo.class, id);
        return Optional.ofNullable(stationInfo);
    }

    public StationInfo findByStationChargerId(String stationChargerId) {
        return Optional.of(em.createQuery("SELECT si FROM StationInfo si WHERE si.stationChargerId = :stationChargerId", StationInfo.class)
                        .setParameter("stationChargerId", stationChargerId)
                        .getSingleResult())
                .orElseThrow(() -> new NotFoundException("적절한 충전소 정보가 없습니다."));
    }

    // 전체 find 메서드
    public List<StationInfo> findAll() {
        return em.createQuery("SELECT si FROM StationInfo si", StationInfo.class)
                .getResultList();
    }

    //TODO: 성능 비교 후 하나로 확정
    public List<StationInfo> updateAllByDirtyChecking(List<StationInfoUpdateDto> requests) {
        long start = System.currentTimeMillis();
        List<StationInfo> stationInfos = new ArrayList<>();
        for (StationInfoUpdateDto request : requests) {
            StationInfo stationInfo = em.createQuery("SELECT si FROM StationInfo si WHERE si.stationChargerId = :stationChargerId", StationInfo.class)
                    .setParameter("stationChargerId", request.getStationChargerId())
                    .getSingleResult();
            stationInfo.updateStationInfo(request);
//            stationInfos.add(stationInfo);
        }
        long end = System.currentTimeMillis();
        log.info("Dirty Checking : {}s", (float) (end - start) / 1000);
        return stationInfos;
    }


    public List<StationInfo> updateAllByJpql(List<StationInfoUpdateDto> requests) {
        long start = System.currentTimeMillis();
        List<StationInfo> stationInfos = new ArrayList<>();
        String jpql = "UPDATE StationInfo si SET " +
                "si.chargerType = :chargerType, " +
                "si.useTime = :useTime, " +
                "si.operatorName = :operatorName, " +
                "si.operatorCall = :operatorCall, " +
                "si.output = :output, " +
                "si.parkingFree = :parkingFree, " +
                "si.notation = :notation, " +
                "si.limitYn = :limitYn, " +
                "si.limitDetail = :limitDetail, " +
                "si.deleteYn = :deleteYn, " +
                "si.deleteDetail = :deleteDetail, " +
                "si.trafficYn = :trafficYn " +
                "WHERE si.stationChargerId = :stationChargerId";
        for (StationInfoUpdateDto request : requests) {
            em.createQuery(jpql)
                    .setParameter("chargerType", request.getChargerType())
                    .setParameter("useTime", request.getUseTime())
                    .setParameter("operatorName", request.getOperatorName())
                    .setParameter("operatorCall", request.getOperatorCall())
                    .setParameter("output", request.getOutput())
                    .setParameter("parkingFree", request.getParkingFree())
                    .setParameter("notation", request.getNotation())
                    .setParameter("limitYn", request.getLimitYn())
                    .setParameter("limitDetail", request.getLimitDetail())
                    .setParameter("deleteYn", request.getDeleteYn())
                    .setParameter("deleteDetail", request.getDeleteDetail())
                    .setParameter("trafficYn", request.getTrafficYn())
                    .setParameter("stationChargerId", request.getStationChargerId())
                    .executeUpdate();
        }
        long end = System.currentTimeMillis();
        log.info("JPQL : {}s", (float) (end - start) / 1000);

        return stationInfos;
    }


    //삭제 메서드
    public StationInfo deleteById(Long id) {
        Optional<StationInfo> findId = findById(id);
        StationInfo stationInfo = findId.orElseThrow(() -> new NotFoundException("적절한 StationInfo가 없습니다."));
        em.remove(stationInfo);
        return stationInfo;
    }
}
