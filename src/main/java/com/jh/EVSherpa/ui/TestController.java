package com.jh.EVSherpa.ui;

import com.jh.EVSherpa.application.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @PostMapping("/api/test")
    public void saveTest(){
        testService.save();
    }
}
