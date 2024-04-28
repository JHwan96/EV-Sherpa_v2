package com.jh.EVSherpa.application;

import com.jh.EVSherpa.domain.Users;
import com.jh.EVSherpa.dto.UserRequestDto;
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

    private UserRequestDto encodePassword(UserRequestDto requestDto) {
        String encode = encoder.encode(requestDto.getPassword());
        return requestDto.encodePassword(encode);
    }
}
