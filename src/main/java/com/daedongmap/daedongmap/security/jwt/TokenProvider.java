package com.daedongmap.daedongmap.security.jwt;

import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.service.UserDetailService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
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

    public String createToken(Users user, List<Authority> roles) {

        Date now = new Date();
        Date accessExpire = new Date(now.getTime() + validTime);

        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", roles);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(accessExpire) // 토큰 유효시각 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 암호화 알고리즘과, secret 값
                .compact();

        //        String refreshToken = Jwts.builder()
//                .setExpiration(new Date(now.getTime() + 60 * 60 * 1000L))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
    }

    public Long getUserID(String token) {
        return parseJwt(token).get("id", Long.class);
    }

    public HttpServletRequest validateToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch(MalformedJwtException e) {
            log.info("인증되지 않은 토큰");
            request.setAttribute("exception", ErrorCode.UNAUTHORIZED_TOKEN);
        } catch(ExpiredJwtException e) {
            log.info("만료된 토큰");
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN);
        } catch(UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰");
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN);
        } catch(Exception e) {
            log.info("유효하지 않은 토큰");
            request.setAttribute("exception", ErrorCode.INVALID_TOKEN);
        }
        return request;
    }

    public Claims parseJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}