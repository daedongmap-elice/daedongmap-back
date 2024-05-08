package com.daedongmap.daedongmap.user.repository;

import com.daedongmap.daedongmap.user.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
