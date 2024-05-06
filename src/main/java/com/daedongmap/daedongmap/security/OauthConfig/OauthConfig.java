package com.daedongmap.daedongmap.security.OauthConfig;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class OauthConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizationGrantType;
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private String userNameAttribute;
}
