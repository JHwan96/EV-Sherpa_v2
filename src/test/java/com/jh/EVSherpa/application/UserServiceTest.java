package com.jh.EVSherpa.application;

import com.jh.EVSherpa.dto.UserRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    @DisplayName("비밀번호 암호화 확인")
    public void checkEncoding() {
        // given
        UserRequestDto build = UserRequestDto.builder()
                .userId("test")
                .password("test")
                .build();

        // when
        userService.joinUser(build);
        Boolean loginYn = userService.login(build);

        // then
        Assertions.assertThat(loginYn).isTrue();
    }
}
