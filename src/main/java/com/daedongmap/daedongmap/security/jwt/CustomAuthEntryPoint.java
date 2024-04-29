package com.daedongmap.daedongmap.security.jwt;

import com.daedongmap.daedongmap.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String exception = null;

        try {
            exception = request.getAttribute("exception").toString();
        } catch (NullPointerException e) {
            log.info("토큰이 존재하지 않습니다.");
        }

        if(exception == null) {
            setResponse(response, ErrorCode.INVALID_TOKEN);
        }
        // 변조, 인증이 불가능한 토큰
        else if(exception.equals(ErrorCode.UNAUTHORIZED_TOKEN.getHttpStatus().toString())) {
            setResponse(response, ErrorCode.UNAUTHORIZED_TOKEN);
        }
        // 만료된 토큰
        else if(exception.equals(ErrorCode.EXPIRED_TOKEN.getHttpStatus().toString())) {
            setResponse(response, ErrorCode.EXPIRED_TOKEN);
        }
        // 지원되지 않는 토큰인 경우
        else if(exception.equals(ErrorCode.UNSUPPORTED_TOKEN.getHttpStatus().toString())) {
            setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
        }
        // 그 외는 유효하지 않은 것으로 간주
        else {
            setResponse(response, ErrorCode.INVALID_TOKEN);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("code", code.getHttpStatus());
        responseJson.put("message", code.getMessage());

        response.getWriter().print(responseJson);
    }
}
