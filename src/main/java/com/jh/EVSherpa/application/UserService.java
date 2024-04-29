package com.jh.EVSherpa.application;

import com.jh.EVSherpa.domain.Users;
import com.jh.EVSherpa.dto.UserRequestDto;
import com.jh.EVSherpa.exception.UserNotFoundException;
import com.jh.EVSherpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    public void joinUser(UserRequestDto requestDto){
        UserRequestDto request = encodePassword(requestDto);
        Users user = Users.of(request);
        userRepository.save(user);
    }

    public UserRequestDto findUserByUserId(String userId) {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 아이디가 없습니다."));
        return user.toDto(user);
    }

    public Boolean login(UserRequestDto requestDto) {
        Users user = userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("유저 아이디가 없습니다."));
        return encoder.matches(requestDto.getPassword(), user.getPassword());
    }

    private UserRequestDto encodePassword(UserRequestDto requestDto) {
        String encode = encoder.encode(requestDto.getPassword());
        return UserRequestDto.builder()
                .userId(requestDto.getUserId())
                .password(encode)
                .build();
    }
}
