package com.jh.EVSherpa.dto.enums;

import lombok.Getter;

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

    ChargerType(String code){
        this.code=code;
    }
}
