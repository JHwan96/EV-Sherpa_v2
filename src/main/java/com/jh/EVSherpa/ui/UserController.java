package com.jh.EVSherpa.ui;

import com.jh.EVSherpa.application.UserService;
import com.jh.EVSherpa.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinUser(@RequestBody UserRequestDto requestDto) {
        userService.joinUser(requestDto);
        log.info(requestDto.getUserId()+ " " + requestDto.getPassword());

        return ResponseEntity.ok().build();
    }
}
