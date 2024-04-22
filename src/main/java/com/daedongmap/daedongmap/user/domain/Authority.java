package com.daedongmap.daedongmap.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String email;

    @JoinColumn(name = "users")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Users user;

    public void setUser(Users user) {
        this.user = user;
    }
}
