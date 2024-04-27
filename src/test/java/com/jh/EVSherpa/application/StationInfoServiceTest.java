package com.jh.EVSherpa.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class StationInfoServiceTest {
    @Autowired
    StationInfoService stationInfoService;

    @Nested
    @DisplayName("StationInfoApi를 호출할 때")
    class StationInfoTest {
        @Test
        @DisplayName("저장 성공 확인")
        public void testStationInfoApiSave() {
            Logger logger = LoggerFactory.getLogger(StationInfoTest.class);

            long start = System.currentTimeMillis();
            int size = stationInfoService.saveStationInfo();
            long end = System.currentTimeMillis();

            System.out.println("count : " + size);
            logger.info("실행 시간 : {}s", (float) (end - start) / 1000);

            Assertions.assertThat(size).isGreaterThan(0);
        }

        @Test
        @DisplayName("수정 성공 확인 (JPQL 사용)")
        public void testStationInfoUpdate() {
            // 충전소 정보 저장 메서드 호출
            stationInfoService.saveStationInfo();
            // 충전소 정보 갱신 메서드 호출
            int stationInfos = stationInfoService.updateStationInfo();
            Assertions.assertThat(stationInfos).isNotEqualTo(0);
        }

        @Test
        @DisplayName("전체 정보 수정 확인")
        public void testStationAllInfoUpdate() {
            // 충전소 정보 저장 메서드 호출
            stationInfoService.saveStationInfo();
            // 충전소 정보 갱신 메서드 호출
            int stationInfos = stationInfoService.updateStationAllInfo();
            Assertions.assertThat(stationInfos).isNotEqualTo(0);
        }
    }


}
