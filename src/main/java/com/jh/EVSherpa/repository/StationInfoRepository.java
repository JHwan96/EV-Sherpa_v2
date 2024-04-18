package com.jh.EVSherpa.repository;

import com.jh.EVSherpa.domain.StationInfo;
import com.jh.EVSherpa.domain.StationStatus;
import com.jh.EVSherpa.dto.StationInfoDto;
import com.jh.EVSherpa.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StationInfoRepository {
    private EntityManager em;

    public StationInfo save(StationInfoDto request) {
        StationStatus stationStatus = StationStatus.fromInfoDto(request);
        StationInfo stationInfo = StationInfo.fromDto(request, stationStatus);
        em.persist(stationInfo);
        return stationInfo;
    }

    //전체 저장 메서드
    public List<StationInfo> saveAll(List<StationInfoDto> requests){
        List<StationInfo> stationInfos = new ArrayList<>();
        for(StationInfoDto request : requests){
            StationStatus stationStatus = StationStatus.fromInfoDto(request);
            StationInfo stationInfo = StationInfo.fromDto(request, stationStatus);
            em.persist(stationInfo);
            stationInfos.add(stationInfo);
        }
        return stationInfos;
    }

    public Optional<StationInfo> findById(Long id) {
        StationInfo stationInfo = em.find(StationInfo.class, id);
        return Optional.ofNullable(stationInfo);
    }

    /**
     * 전체 find 메서드
     *
     * @return 찾은 StationInfo 리스트
     */
    public List<StationInfo> findAll() {
        return em.createQuery("SELECT si FROM StationInfo si", StationInfo.class)
                .getResultList();
    }

    //TODO: update 필요할 때 작성
//    public StationInfo updateById(StationInfoUpdateDto request, Long id)

    /**
     * 삭제 메서드
     *
     * @param id
     * @return 삭제된 StationInfo
     */
    public StationInfo deleteById(Long id) {
        Optional<StationInfo> findId = findById(id);
        StationInfo stationInfo = findId.orElseThrow(() -> new NotFoundException("적절한 StationInfo가 없습니다."));
        em.remove(stationInfo);
        return stationInfo;
    }
}
