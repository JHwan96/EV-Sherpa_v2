package com.jh.EVSherpa.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChargerApiServiceTest {
    @Autowired
    ChargerApiService chargerApiService;

    @Test
    public void test() {
        int test = chargerApiService.test();
        Assertions.assertThat(test).isEqualTo(10);
    }
}
