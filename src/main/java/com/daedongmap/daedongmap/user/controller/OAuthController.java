package com.daedongmap.daedongmap.user.controller;

import com.daedongmap.daedongmap.user.dto.response.JwtTokenDto;
import com.daedongmap.daedongmap.user.service.OauthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class OAuthController {

    private final OauthService oauthService;

    @GetMapping("/naver")
    public ResponseEntity<JwtTokenDto> naverLogin(@RequestParam(name = "code") String code,
                                                  @RequestParam(name = "state", required = false) String state) {

        return ResponseEntity.ok().body(oauthService.signUpAndLogin(code, "naver"));
    }
}
