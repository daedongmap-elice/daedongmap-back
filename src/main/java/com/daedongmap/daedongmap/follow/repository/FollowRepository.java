package com.daedongmap.daedongmap.follow.repository;

import com.daedongmap.daedongmap.follow.model.Follow;
import com.daedongmap.daedongmap.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(Users follower, Users following);
    Follow findByFollowerAndFollowing(Users follower, Users following);
}
