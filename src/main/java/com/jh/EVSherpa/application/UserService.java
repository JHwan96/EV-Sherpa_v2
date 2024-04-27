package com.jh.EVSherpa.application;

import com.jh.EVSherpa.domain.Users;
import com.jh.EVSherpa.dto.UserRequestDto;
import com.jh.EVSherpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public void joinUser(UserRequestDto requestDto){
        Users user = Users.of(requestDto);
        userRepository.save(user);
    }
}
