package com.daedongmap.daedongmap.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://localhost:8080",
                        "http://35.232.243.53:8080",
                        "http://kdt-cloud-1-team03.elicecoding.com",
                        "http://kdt-cloud-1-team03.elicecoding.com:3002",
                        "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowCredentials(true)
                .maxAge(3000);
    }
}
