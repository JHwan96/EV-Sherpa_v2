package com.jh.EVSherpa.application;

import com.jh.EVSherpa.domain.StationStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class StationApiServiceTest {
    @Autowired
    StationApiService stationApiService;

    @Test
    public void test() {
        int test = stationApiService.test();
        Assertions.assertThat(test).isEqualTo(10);
    }

    @Nested
    @DisplayName("StationInfoApi를 호출할 때")
    class StationInfoTest {
        @Test
        @DisplayName("저장 성공 확인")
        public void testStationInfoApiSave() {
            int size = stationApiService.saveStationInfo();
            System.out.println("count : " + size);
            Assertions.assertThat(size).isGreaterThan(0);
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
            Assertions.assertThat(stationStatuses).isNotEmpty();
        }
    }
}
