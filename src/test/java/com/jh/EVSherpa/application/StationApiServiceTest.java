package com.jh.EVSherpa.application;

import com.jh.EVSherpa.domain.StationInfo;
import com.jh.EVSherpa.domain.StationStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@Transactional
class StationApiServiceTest {
    @Autowired
    StationApiService stationApiService;

    @Nested
    @DisplayName("StationInfoApi를 호출할 때")
    class StationInfoTest {
        @Test
        @DisplayName("저장 성공 확인")
        public void testStationInfoApiSave() {
            Logger logger = LoggerFactory.getLogger(StationInfoTest.class);

            long start = System.currentTimeMillis();
            int size = stationApiService.saveStationInfo();
            long end = System.currentTimeMillis();

            System.out.println("count : " + size);
            logger.info("실행 시간 : {}s", (float)(end - start) / 1000);

            Assertions.assertThat(size).isGreaterThan(0);
        }

        @Test
        @DisplayName("수정 성공 확인 (더티 체킹 사용)")
        public void testStationInfoUpdateDirtyChecking(){
            Logger logger = LoggerFactory.getLogger(StationInfoTest.class);
            long start = System.currentTimeMillis();
            // 충전소 정보 저장 메서드 호출
            stationApiService.saveStationInfo();
            long end = System.currentTimeMillis();
            logger.info("saveStationInfo : {}s", (float)(end-start)/1000);
            // 충전소 정보 갱신 메서드 호출
            List<StationInfo> stationInfos = stationApiService.updateStationInfoDirtyCheck();
            Assertions.assertThat(stationInfos).isEmpty();
        }

        @Test
        @DisplayName("수정 성공 확인 (JPQL 사용)")
        public void testStationInfoUpdateJpql(){
            Logger logger = LoggerFactory.getLogger(StationInfoTest.class);
            long start = System.currentTimeMillis();
            // 충전소 정보 저장 메서드 호출
            stationApiService.saveStationInfo();
            long end = System.currentTimeMillis();
            logger.info("saveStationInfo : {}s", (float)(end-start)/1000);

            List<StationInfo> stationInfos = stationApiService.updateStationInfoJpql();
            Assertions.assertThat(stationInfos).isEmpty();
        }
    }

    @Nested
    @DisplayName("StationStatusApi를 호출할 때")
    class StationStatusTest {
        @Test
        @DisplayName("저장 성공 확인")
        public void testStationStatusApiSave(){
            List<StationStatus> stationStatuses = stationApiService.saveStationStatus();
            System.out.println(stationStatuses.get(0).getStationChargerId());
            Assertions.assertThat(stationStatuses).isEmpty();
        }
    }
}
