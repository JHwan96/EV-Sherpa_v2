package com.jh.EVSherpa.dto.enums;

import com.jh.EVSherpa.exception.NotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ChargerType {
    DC_CHA("01"),
    AC_NORMAL("02"),
    DC_CHA_AC3_SANG("03"),
    DC_COMBO("04"),
    DC_CHA_DC_COMBO("05"),
    DC_CHA_AC3_SANG_DC_COMBO("06"),
    AC3_SANG("07");

    private final String code;

    ChargerType(String code) {
        this.code = code;
    }

    public static ChargerType of(String chgerType) {
        return Arrays.stream(ChargerType.values())
                .filter(chargerType -> chargerType.getCode().equals(chgerType))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("적절한 충전 타입이 없습니다."));
    }
}
