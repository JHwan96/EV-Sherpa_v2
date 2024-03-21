package com.jh.EVSherpa.login.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id", nullable = false)
    private Long id;

    @Column(name = "age")
    private Integer age;

    @Column(name = "car_name", length = 16)
    private String carName;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Builder
    public UserInfo(Integer age, String carName, String nickname) {
        this.age = age;
        this.carName = carName;
        this.nickname = nickname;
    }
}
