package com.jh.EVSherpa.dto.enums;

import lombok.Getter;

@Getter
public enum ChargerStat {
    ERROR(1),
    WAIT(2),
    ING(3),
    STOP(4),
    CHECKING(5),
    UNIDENTIFIED(9);

    private final int code;

    ChargerStat(int code) {
        this.code = code;
    }
}
