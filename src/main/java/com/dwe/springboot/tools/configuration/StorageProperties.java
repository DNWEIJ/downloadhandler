package com.dwe.springboot.tools.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("app.storage")
public record StorageProperties(
    String location
){}