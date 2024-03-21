package com.jh.EVSherpa.login.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class UserRequestDto {
    private String userId;
    private String password;

    @Builder
    public UserRequestDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
