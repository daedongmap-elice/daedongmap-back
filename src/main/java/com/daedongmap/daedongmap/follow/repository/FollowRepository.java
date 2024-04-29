package com.daedongmap.daedongmap.follow.repository;

import com.daedongmap.daedongmap.follow.model.Follow;
import com.daedongmap.daedongmap.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(Users follower, Users following);
    Follow findByFollowerAndFollowing(Users follower, Users following);
    List<Follow> findAllByFollower(Users follower);
    List<Follow> findAllByFollowing(Users following);
}
