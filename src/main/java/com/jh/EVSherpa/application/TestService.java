package com.jh.EVSherpa.application;

import com.jh.EVSherpa.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TestService {
    private final TestRepository testRepository;

    public void save() {
        testRepository.save();
    }
}
