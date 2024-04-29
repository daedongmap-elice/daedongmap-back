package com.daedongmap.daedongmap.security.jwt;

import com.daedongmap.daedongmap.exception.ErrorCode;
import com.daedongmap.daedongmap.user.service.UserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailService userDetailService;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                tokenProvider.validateToken(token, request);
                Long userId = tokenProvider.getUserID(token);

                UserDetails userDetails = userDetailService.loadUserByUsername(userId.toString());

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        // 변조, 인증 불가
        } catch (MalformedJwtException e){
            request.setAttribute("exception", ErrorCode.UNAUTHORIZED_TOKEN.getHttpStatus());
        // 만료 토큰
        } catch (ExpiredJwtException e){
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getHttpStatus());
        // 지원되지 않는 토큰
        } catch (UnsupportedJwtException e){
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getHttpStatus());
        // 그 외 인증이 불가능한 토큰
        } catch (Exception e) {
            request.setAttribute("exception", ErrorCode.INVALID_TOKEN.getHttpStatus());
        }

        filterChain.doFilter(request, response);
    }
}
