package com.jh.EVSherpa.domain;

import com.jh.EVSherpa.dto.UserRequestDto;
import com.jh.EVSherpa.global.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_id", length = 16, nullable = false, unique = true)
    private String userId;

    @Column(name = "password", length = 64, nullable = false)
    private String password;

    //사용자 세부정보를 합칠지 생각

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_info_id")
    private UserInfo userInfo;

    @Builder
    public Users(String userId, String password, UserInfo userInfo) {
        this.userId = userId;
        this.password = password;
        this.userInfo = userInfo;
    }

    public static Users of(UserRequestDto requestDto) {
        return Users.builder()
                .userId(requestDto.getUserId())
                .password(requestDto.getPassword())
                .build();
    }
    public UserRequestDto toDto(Users user){
        return UserRequestDto.builder()
                .userId(user.getUserId())
                .password(user.getPassword())
                .build();
    }
}
