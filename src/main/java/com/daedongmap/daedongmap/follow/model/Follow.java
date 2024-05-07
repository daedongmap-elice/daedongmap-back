package com.daedongmap.daedongmap.follow.model;

import com.daedongmap.daedongmap.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="follower_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users follower; // 팔로우 하는 유저

    @ManyToOne
    @JoinColumn(name="following_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Users following; // 팔로우 받는 유저

    @Builder
    public Follow(Users follower, Users following) {
        this.follower = follower;
        this.following = following;
    }

}
