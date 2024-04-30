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
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 7, message = "비밀번호는 최소 8자리 이상이어야 합니다")
    private String password;

    @Column(name = "status")
    @Size(max = 50, message = "상태는 50자 이내로 작성 가능합니다.")
    private String status;

    @Column(unique = true, name = "email")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Column(name = "phone_number")
    @NotBlank(message = "전화번호를 입력해주세요")
//    @Pattern(regexp = "([0-9]{10,11})")
    private String phoneNumber;

    @Column(name = "web_site")
    private String webSite;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Authority> roles = new ArrayList<>();

    @Builder
    public Users(String nickName, String email, String webSite, String status, String phoneNumber, String password, List<Authority> role) {
        this.nickName = nickName;
        this.status = status;
        this.email = email;
        this.webSite = webSite;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.roles = role;
        role.forEach(o -> o.setUser(this));
    }

    public void updateUser(UserUpdateDto userUpdateDto) {
        this.nickName = userUpdateDto.getNickName();
        this.status = userUpdateDto.getStatus();
        this.email = userUpdateDto.getEmail();
        this.webSite = userUpdateDto.getWebSite();
        this.password = userUpdateDto.getPassword();
        this.phoneNumber = userUpdateDto.getPhoneNumber();
    }
}
