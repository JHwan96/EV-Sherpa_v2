package com.jh.EVSherpa.repository;

import com.jh.EVSherpa.domain.Test;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TestRepository {
    private final EntityManager em;

    public void save(){
        for(int i = 0 ; i < 20; i++){
            Test test = Test.createTest(i);
            em.persist(test);
        }
        em.flush();
        em.clear();
    }
}
