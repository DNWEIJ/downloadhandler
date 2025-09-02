package com.dwe.springboot.tutorial.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.car")
public record CarProperties(
        String location,
        String file
) {
}