package com.daedongmap.daedongmap.jwt;

import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.dto.response.JwtTokenDto;
import com.daedongmap.daedongmap.user.service.UserDetailService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    // The signing key's size is 48 bits which is not secure enough for the HS256 algorithm
    // 영어 한단어당 8bit, 32글자 이상이어야 에러가 발생하지 않는다
    // 배포 단계에서는 노출되지 않도록 환경변수로 등록할 것
    private String secretKey = "5524A1967CB7B4E95F6C728886A81QKW3M";

    private static final long validTime = 30 * 60 * 1000L;

    private final UserDetailService userDetailService;

    // secretKey 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public JwtTokenDto createToken(String user, List<Authority> roles) {
        Date now = new Date();
        Date accessExpire = new Date(now.getTime() + validTime);

        Claims claims = Jwts.claims().setSubject(user);
        claims.put("roles", roles);

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(accessExpire) // 토큰 유효시각 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘과, secret 값
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + 60 * 60 * 1000L))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return JwtTokenDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpire(accessExpire.getTime())
                .build();
    }

    public Authentication getAuth(String token) {

        UserDetails userDetails = userDetailService
                .loadUserByUsername(this.getAccount(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );
    }

    public String getAccount(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request
                .getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        try {
            if(!token.substring(0, "BEARER".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
