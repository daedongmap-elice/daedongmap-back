package com.daedongmap.daedongmap.user.domain;

import com.daedongmap.daedongmap.user.dto.UserUpdateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    @NotBlank(message = "닉네임은 필수 입력사항입니다!")
    private String nickName;

    @Column(name = "password")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다")
    private String password;

    @Column(name = "status")
    @Size(max = 50, message = "상태는 50자 이내로 작성 가능합니다.")
    private String status;

    @Column(name = "email")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "modified_at")
    @LastModifiedDate
    private Date modifiedAt;

    @Builder
    public Users(String nickName, String email, String status) {
        this.nickName = nickName;
        this.status = status;
        this.email = email;
        this.authority = Authority.ROLE_USER;
    }

    public void updateUser(UserUpdateDto userUpdateDto) {
        this.nickName = userUpdateDto.getNickName();
        this.status = userUpdateDto.getStatus();
        this.password = userUpdateDto.getPassword();
        this.email = userUpdateDto.getEmail();
    }
}
