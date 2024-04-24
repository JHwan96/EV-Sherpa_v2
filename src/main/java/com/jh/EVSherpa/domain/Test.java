package com.jh.EVSherpa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@SequenceGenerator(
        name = "TEST_SEQ_GENERATOR",
        sequenceName = "TEST_SEQ",
        allocationSize = 100
)
@Builder
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_SEQ_GENERATOR")
    private Long id;

    private String name;

    public static Test createTest(int i) {
        Test test = Test.builder()
                .name("name" + i)
                .build();
        return test;
    }

}
