package com.daedongmap.daedongmap.user.domain;

import com.daedongmap.daedongmap.common.entity.BaseTimeEntity;
import com.daedongmap.daedongmap.user.dto.request.UserUpdateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    @NotBlank(message = "닉네임은 필수 입력사항입니다!")
    private String nickName;

    @Column(name = "password")
    @Size(min = 7, message = "비밀번호는 최소 8자리 이상이어야 합니다")
    private String password;

    @Column(name = "status")
    @Size(max = 50, message = "상태는 50자 이내로 작성 가능합니다.")
    private String status;

    @Column(name = "email")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Column(name = "phone_number")
//    @Pattern(regexp = "([0-9]{10,11})")
    private String phoneNumber;

    @Column(name = "web_site")
    private String webSite;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "is_member")
    private Boolean isMember;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Authority> roles = new ArrayList<>();

    @Builder
    public Users(String nickName, String status, String email,
                 String webSite, String phoneNumber, String profileImage,
                 String password, Boolean isMember, List<Authority> role) {
        this.nickName = nickName;
        this.status = status;
        this.email = email;
        this.webSite = webSite;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.password = password;
        this.isMember = isMember;
        this.roles = role;
        role.forEach(o -> o.setUser(this));
    }

    public void updateUser(UserUpdateDto userUpdateDto) {
        this.nickName = userUpdateDto.getNickName();
        this.status = userUpdateDto.getStatus();
        this.webSite = userUpdateDto.getWebSite();
        this.profileImage = userUpdateDto.getProfileImageLink();
    }
}
