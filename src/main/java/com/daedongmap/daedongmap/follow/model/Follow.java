package com.daedongmap.daedongmap.follow.model;

import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="follower_id")
    private Users follower; // 팔로우 하는 유저

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="following_id")
    private Users following; // 팔로우 받는 유저

    @Builder
    public Follow(Users follower, Users following) {
        this.follower = follower;
        this.following = following;
    }

}
