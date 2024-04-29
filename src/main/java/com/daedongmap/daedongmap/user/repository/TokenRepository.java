package com.daedongmap.daedongmap.user.repository;

import com.daedongmap.daedongmap.user.domain.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshTokens, Long> {
    void deleteByUserId(Long id);

    Optional<RefreshTokens> findByRefreshToken(String refreshToken);
}
