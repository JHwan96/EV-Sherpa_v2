package com.jh.EVSherpa.dto.enums;

import com.jh.EVSherpa.exception.NotFoundException;

public enum ChargerMethod {
    SINGLE,
    CONCURRENT;

    public static ChargerMethod of(String method) {
        if (method.equals("단독")) {
            return ChargerMethod.SINGLE;
        } else if (method.equals("동시")) {
            return ChargerMethod.CONCURRENT;
        } else {
            throw new NotFoundException("일치하는 충전 방식이 없습니다.");
        }
    }
}
