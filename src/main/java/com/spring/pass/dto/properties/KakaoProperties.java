package com.spring.pass.dto.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KakaoProperties(
        String host,
        String token
) {
}
