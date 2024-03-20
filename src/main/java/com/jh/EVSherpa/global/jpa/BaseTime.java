package com.jh.EVSherpa.global.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(EnableJpaAuditing.class)
public class BaseTime {
    @CreatedDate
    @Column(name = "REGISTER_DATE", nullable = false)
    private LocalDateTime registerDate;

    @LastModifiedDate
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;
}
