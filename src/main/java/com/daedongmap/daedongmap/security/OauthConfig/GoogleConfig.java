package com.daedongmap.daedongmap.security.OauthConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public class GoogleConfig {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizationGrantType;
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private String userNameAttribute;
}