package com.daedongmap.daedongmap.security.OauthConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.naver")
public class NaverConfig extends OauthConfig {

}
