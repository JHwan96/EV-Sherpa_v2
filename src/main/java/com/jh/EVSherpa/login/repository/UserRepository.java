package com.jh.EVSherpa.login.repository;

import com.jh.EVSherpa.login.domain.User;
import com.jh.EVSherpa.login.dto.UserRequestDto;
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

    public User save(UserRequestDto request) {
        return User.builder()
                .userId(request.getUserId())
                .password(request.getPassword())
                .build();
    }

    public Optional<User> findById(UUID id) {
        User findUser = em.find(User.class, id);
        return Optional.of(findUser);
    }

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    public void deleteById(UUID id) {
        User deleteUser = Optional.ofNullable(em.find(User.class, id))
                .orElseThrow(() -> new EntityNotFoundException("찾을 수 없습니다."));
        em.remove(deleteUser);
    }

}
