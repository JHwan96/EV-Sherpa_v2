package com.jh.EVSherpa.repository;

import com.jh.EVSherpa.domain.Users;
import com.jh.EVSherpa.dto.UserRequestDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public Users save(UserRequestDto request) {
        return Users.builder()
                .userId(request.getUserId())
                .password(request.getPassword())
                .build();
    }

    public Optional<Users> findById(UUID id) {
        Users findUser = em.find(Users.class, id);
        return Optional.of(findUser);
    }

    public List<Users> findAll() {
        return em.createQuery("SELECT u FROM User u", Users.class)
                .getResultList();
    }

    public void deleteById(UUID id) {
        Users deleteUser = Optional.ofNullable(em.find(Users.class, id))
                .orElseThrow(() -> new EntityNotFoundException("찾을 수 없습니다."));
        em.remove(deleteUser);
    }

}
