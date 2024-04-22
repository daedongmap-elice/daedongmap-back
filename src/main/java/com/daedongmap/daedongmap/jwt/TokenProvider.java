package com.daedongmap.daedongmap.jwt;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.JwtTokenDto;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.hibernate.mapping.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    // The signing key's size is 48 bits which is not secure enough for the HS256 algorithm
    // 영어 한단어당 8bit, 32글자 이상이어야 에러가 발생하지 않는다
    // 배포 단계에서는 노출되지 않도록 환경변수로 등록할 것
    private String secretKey = "5524A1967CB7B4E95F6C728886A81QKW3M";

    private static final String BEARER_TYPE = "bearer";
    private static final long validTime = 30 * 60 * 1000L;

    // secretKey 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public JwtTokenDto createToken(Authentication auth) {
        Date now = new Date();

        String authorities = auth.getAuthorities().stream().map

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim("id", users.getId()) // 정보 저장
                .claim("role", Authority.ROLE_USER)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + validTime)) // 토큰 유효시각 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘과, secret 값
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + 60 * 60 * 1000L))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuth(String token) {
        Claims claims = getClaims(token);

        if(claims.get("role") == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
