package com.daedongmap.daedongmap.security.jwt;

import com.daedongmap.daedongmap.exception.CustomException;
import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.user.domain.Authority;
import com.daedongmap.daedongmap.user.domain.RefreshTokens;
import com.daedongmap.daedongmap.user.domain.Users;
import com.daedongmap.daedongmap.user.dto.response.JwtTokenDto;
import com.daedongmap.daedongmap.user.service.TokenService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${jwt.encoder}")
    private String SECRET_KEY;

    // 30분 리프레시 access 토큰 유효시간
    private static final long EXPIRE_IN = 30 * 60 * 1000L;
    // 1주 간의 리프레시 토큰 유효시간
    private static final long REFRESH_EXPIRE_IN = 7 * 24 * 60 * 60 * 1000L;

    private final TokenService tokenService;
    private String accessToken;
    private Date accessExpire;


    // secretKey 인코딩
    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public JwtTokenDto createToken(Users user, List<Authority> roles) {

        String refreshToken;
        Date issuedAt = new Date();
        accessExpire = new Date(issuedAt.getTime() + EXPIRE_IN);

        try{
            Claims claims = Jwts.claims();
            claims.put("id", user.getId());
            claims.put("email", user.getEmail());
            claims.put("roles", roles);

            accessToken = Jwts.builder()
                    .setSubject(user.getEmail())
                    .setClaims(claims)
                    .setIssuedAt(issuedAt) // 토큰 발행 시간 정보
                    .setExpiration(accessExpire) // 토큰 유효시각 설정
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 암호화 알고리즘과, secret 값
                    .compact();

            refreshToken = Jwts.builder()
                    .setExpiration(new Date(issuedAt.getTime() + REFRESH_EXPIRE_IN))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();

            RefreshTokens toSaveToken = RefreshTokens.builder()
                    .userId(user.getId())
                    .refreshToken(refreshToken)
                    .build();

            tokenService.deleteRefreshByUserId(user.getId());
            if(!tokenService.save(toSaveToken)) {
                throw new CustomException(ErrorCode.TOKEN_ERROR);
            }
            log.info(user.getEmail() + " 사용자에 대한 토큰 발급 완료");

        } catch(Exception e) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public JwtTokenDto createNewAccessToken(Users user, List<Authority> roles) {

        Date now = new Date();
        accessExpire = new Date(now.getTime() + EXPIRE_IN);

        try {
            Claims claims = Jwts.claims();
            claims.put("id", user.getId());
            claims.put("email", user.getEmail());
            claims.put("roles", roles);

            accessToken = Jwts.builder()
                    .setSubject(user.getEmail())
                    .setClaims(claims)
                    .setIssuedAt(now) // 토큰 발행 시간 정보
                    .setExpiration(accessExpire) // 토큰 유효시각 설정
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 암호화 알고리즘과, secret 값
                    .compact();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.TOKEN_ERROR);
        }

        return JwtTokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    public Long getUserID(String token) {
        return parseJwt(token).get("id", Long.class);
    }

    public HttpServletRequest validateToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
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
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
