package com.jh.EVSherpa.application;

import com.jh.EVSherpa.domain.StationInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class StationApiServiceTest {
    @Autowired
    StationApiService stationApiService;

    @Test
    public void test() {
        int test = stationApiService.test();
        Assertions.assertThat(test).isEqualTo(10);
    }

    @Test
    @Transactional
    @DisplayName("StationInfoApi 저장 확인")
    public void testStationInfoApiSave(){
        List<StationInfo> stationInfos = stationApiService.saveStationInfo();
        System.out.println(stationInfos.get(0).getAddress());
        Assertions.assertThat(stationInfos).isNotEmpty();
    }
}
