package com.jh.EVSherpa.dto.enums;

import com.jh.EVSherpa.exception.NotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ChargerStatus {
    UNKNOWN(0),
    ERROR(1),
    FREE(2),
    ING(3),
    STOP(4),
    CHECKING(5),
    UNIDENTIFIED(9);

    private final int code;

    ChargerStatus(int code) {
        this.code = code;
    }

    public static ChargerStatus of(String chargerStatus) {
        return Arrays.stream(ChargerStatus.values())
                .filter(status -> status.getCode() == Integer.parseInt(chargerStatus))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("일치하는 충전 상태가 없습니다."));
    }
}
