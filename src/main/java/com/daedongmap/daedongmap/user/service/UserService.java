package com.daedongmap.daedongmap.user.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.security.jwt.TokenProvider;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.request.UserLoginDto;
import com.daedongmap.daedongmap.user.dto.request.UserRegisterDto;
import com.daedongmap.daedongmap.user.dto.request.UserUpdateDto;
import com.daedongmap.daedongmap.user.dto.response.AuthResponseDto;
import com.daedongmap.daedongmap.user.dto.response.JwtTokenDto;
import com.daedongmap.daedongmap.user.dto.response.UserResponseDto;
import com.daedongmap.daedongmap.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final String DEFAULT_PROFILE = "https://s3.ap-northeast-2.amazonaws.com/daedongmap-bucket/profile/canelo%40gmail.com";

    @Transactional
    public AuthResponseDto registerUser(UserRegisterDto userRegisterDto) {

        userRepository.findByEmail(userRegisterDto.getEmail()).ifPresent(e -> {
            throw new CustomException(ErrorCode.EMAIL_IN_USE);
        });

        boolean isMember = userRegisterDto.getPassword() != null;

        if(userRegisterDto.getProfileImage() == null) {
            userRegisterDto.setProfileImage(DEFAULT_PROFILE);
        }

        Users newUser = Users.builder()
                .nickName(userRegisterDto.getNickName())
                .status("안녕하세요! 반갑습니다!")
                .email(userRegisterDto.getEmail())
                .webSite("아직 연결된 외부 사이트가 없습니다.")
                .phoneNumber(fixPhoneNumber(userRegisterDto.getPhoneNumber()))
                .profileImage(userRegisterDto.getProfileImage())
                .password(encodePassword(userRegisterDto.getPassword()))
                .isMember(isMember)
                .role(Collections.singletonList(Authority.builder().role("ROLE_USER").build()))
                .build();

        userRepository.save(newUser);

        return new AuthResponseDto(newUser.getNickName(), newUser, newUser.getRoles());
    }

    public String encodePassword(String password) {
        if(password != null) {
            return passwordEncoder.encode(password);
        }
        return null;
    }

    public String fixPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) {
            return null;
        } else {
            return phoneNumber.replaceAll("-", "");
        }
    }

    @Transactional
    public JwtTokenDto loginUser(UserLoginDto userLoginDto) {

        Users foundUser = userRepository.findByEmail(userLoginDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!foundUser.getIsMember()) {
            throw new CustomException(ErrorCode.OAUTH_USER);
        }

        // matches(입력된 비밀번호, 암호화된 비밀번호)
        if(!passwordEncoder.matches(userLoginDto.getPassword(), foundUser.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        return tokenProvider.createToken(foundUser, foundUser.getRoles());
    }

    public String retrieveUserId(String phoneNumber) {

        Users foundUser = userRepository.findByPhoneNumber(phoneNumber.replaceAll("-", ""))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!foundUser.getIsMember()) {
            return "간편로그인 사용자십니다! 간편로그인을 사용해주세요!";
        }

        return foundUser.getEmail();
    }

    public UserResponseDto returnUserDtoById(Long userId) {

        Users foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserResponseDto.builder()
                .user(foundUser)
                .build();
    }

    public Users findUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Users findUserByEmail(String email) {

        Optional<Users> foundUser = userRepository.findByEmail(email);

        return foundUser.orElse(null);

    }

    @Transactional
    public UserResponseDto updateUser(Long userId, MultipartFile profileImage, UserUpdateDto userUpdateDto) throws IOException {

        Users foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(profileImage != null) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(profileImage.getSize());
            metadata.setContentType(profileImage.getContentType());

            amazonS3Client.putObject(
                    bucket + "/profile",
                    foundUser.getEmail(),
                    profileImage.getInputStream(), metadata
            );

            String imageUrl = amazonS3Client.getUrl(bucket + "/profile", foundUser.getEmail()).toString();
            userUpdateDto.setProfileImageLink(imageUrl);
        } else {
            userUpdateDto.setProfileImageLink(foundUser.getProfileImage());
        }

        foundUser.updateUser(userUpdateDto);

        // @Transactional 어노테이션을 붙이면 JPA에서 트랜잭션이 끝나는 시점에서 변화가 생긴 엔티티를 모두 자동으로 반영
        // 조회 시 스냅샷을 만들고 종료 시 스냅샷과 차이가 있다면 DB에 이를 반영한다.
//        userRepository.save(foundUser);

        return UserResponseDto.builder().user(foundUser).build();
    }

    @Transactional
    public String deleteUser(Long userId) {

        Users deletedUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.deleteById(userId);

        return deletedUser.getNickName();
    }

}
