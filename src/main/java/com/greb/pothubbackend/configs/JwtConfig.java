package com.greb.pothubbackend.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="jwt")
public record JwtConfig(
        String header,
        String prefix,
        String secret,
        Integer accessExpiration,
        Integer refreshExpiration) {
}
