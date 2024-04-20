package com.jh.EVSherpa.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
}
