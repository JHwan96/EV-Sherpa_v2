package com.jh.EVSherpa.repository;

import com.jh.EVSherpa.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
