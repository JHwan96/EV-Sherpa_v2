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
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StationInfoRepository {
    private final EntityManager em;

    //전체 저장 메서드
    public int saveAll(List<StationInfoDto> requests) {
        int count = 0;
        for (StationInfoDto request : requests) {
            StationInfo saveStationInfo = save(request);
            count++;
        }
        em.flush();
        em.clear();
        return count;
    }

    public int saveAllList(List<List<StationInfoDto>> requestList) {
        int totalSaveCount = 0;
        for (List<StationInfoDto> requests : requestList) {
            int count = 0;
            for (StationInfoDto request : requests) {
                StationInfo saveStationInfo = save(request);
                count++;
            }
            log.info("saveAllList : {}", count);
            em.flush();
            em.clear();
            totalSaveCount += count;
        }
        return totalSaveCount;
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

    // 전체 stationChargerId find 메서드
    public List<String> findAllStationChargerId() {
        long start = System.currentTimeMillis();
        List<String> resultList = em.createQuery("SELECT si.stationChargerId FROM StationInfo si", String.class)
                .getResultList();
        long end = System.currentTimeMillis();
        log.info("findAll time : {}s", (float)(end-start)/1000);
        return resultList;
    }

    public List<StationInfo> findAll() {
        long start = System.currentTimeMillis();
        List<StationInfo> resultList = em.createQuery("SELECT si FROM StationInfo si", StationInfo.class)
                .getResultList();
        long end = System.currentTimeMillis();
        log.info("findAll time : {}s", (float)(end-start)/1000);
        return resultList;
    }

    String JPQL = "UPDATE StationInfo si SET " +
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
    public int updateAll(List<StationInfoUpdateDto> requests) {
        int count = 0;
        for (StationInfoUpdateDto request : requests) {
            int i = em.createQuery(JPQL)
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
            count += i;
        }
        log.info("update count : {}", count);
        return count;
    }

    public int addUpdateList(List<StationInfoDto> requests){
        requests.stream()
                .map(m -> m.getStationChargerId());
        return 1;
    }

    public int updateAllList(List<List<StationInfoUpdateDto>> requestList) {
        int totalCount = 0;
        for (List<StationInfoUpdateDto> requests : requestList) {
            int count = 0;
            for (StationInfoUpdateDto request : requests) {

                int i = em.createQuery(JPQL)
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
                count += i;
            }
            totalCount += count;
        }
        log.info("update count : {}", totalCount);
        return totalCount;
    }

    // 전체 정보 갱신 메서드 (7~30일 마다 한번씩 갱신)
    // TODO: 효용성 있는지 차후 확인
    public int updateAllInfo(List<List<StationInfoDto>> requestList) {
        int totalCount = 0;
        String jpql = "UPDATE StationInfo si SET " +
                "si.stationName = :stationName, " +
                "si.stationChargerId = :stationChargerId, " +
                "si.chargerType = :chargerType, " +
                "si.address = :address, " +
                "si.location = :location, " +
                "si.pointer = :position, " +
                "si.useTime = :useTime, " +
                "si.businessId = :businessId, " +
                "si.businessName = :businessName, " +
                "si.operatorName = :operatorName, " +
                "si.operatorCall = :operatorCall, " +
                "si.output = :output, " +
                "si.chargerMethod = :chargerMethod, " +
                "si.zcode = :zcode, " +
                "si.zscode = :zscode, " +
                "si.kind = :kind, " +
                "si.kindDetail = :kindDetail, " +
                "si.parkingFree = :parkingFree, " +
                "si.notation = :notation, " +
                "si.limitYn = :limitYn, " +
                "si.limitDetail = :limitDetail, " +
                "si.deleteYn = :deleteYn, " +
                "si.deleteDetail = :deleteDetail, " +
                "si.trafficYn = :trafficYn " +
                "WHERE si.stationChargerId = :stationChargerId";
        List<StationInfoDto> saveList = new ArrayList<>();

        for(List<StationInfoDto> requests : requestList) {
            int count = 0;
            for (StationInfoDto request : requests) {
                int i = em.createQuery(jpql)
                        .setParameter("stationName", request.getStationName())
                        .setParameter("stationChargerId", request.getStationChargerId())
                        .setParameter("chargerType", request.getChargerType())
                        .setParameter("address", request.getAddress())
                        .setParameter("location", request.getLocation())
                        .setParameter("position", request.getPosition())
                        .setParameter("useTime", request.getUseTime())
                        .setParameter("businessId", request.getBusinessId())
                        .setParameter("businessName", request.getBusinessName())
                        .setParameter("operatorName", request.getOperatorName())
                        .setParameter("operatorCall", request.getOperatorCall())
                        .setParameter("output", request.getOutput())
                        .setParameter("chargerMethod", request.getChargerMethod())
                        .setParameter("zcode", request.getZcode())
                        .setParameter("zscode", request.getZscode())
                        .setParameter("kind", request.getKind())
                        .setParameter("kindDetail", request.getKindDetail())
                        .setParameter("parkingFree", request.getParkingFree())
                        .setParameter("notation", request.getNotation())
                        .setParameter("limitYn", request.getLimitYn())
                        .setParameter("limitDetail", request.getLimitDetail())
                        .setParameter("deleteYn", request.getDeleteYn())
                        .setParameter("deleteDetail", request.getDeleteDetail())
                        .setParameter("trafficYn", request.getTrafficYn())
                        .setParameter("stationChargerId", request.getStationChargerId())
                        .executeUpdate();
                count += i;
            }
            totalCount += count;
        }
        log.info("update count : {}", totalCount);
        return totalCount;
    }

    //삭제 메서드
    public StationInfo deleteById(Long id) {
        Optional<StationInfo> findId = findById(id);
        StationInfo stationInfo = findId.orElseThrow(() -> new NotFoundException("적절한 StationInfo가 없습니다."));
        em.remove(stationInfo);
        return stationInfo;
    }

    public int deleteByAllStationChargerId(Set<String> stationChargerIdList){
        return em.createQuery("DELETE FROM StationInfo si where si.stationChargerId IN :stationChargerIdList")
                .setParameter("stationChargerIdList", stationChargerIdList)
                .executeUpdate();
    }

    private StationInfo save(StationInfoDto request) {
        StationStatus stationStatus = StationStatus.fromInfoDto(request);
        StationInfo stationInfo = StationInfo.fromDto(request, stationStatus);
        em.persist(stationInfo);
        return stationInfo;
    }
}
