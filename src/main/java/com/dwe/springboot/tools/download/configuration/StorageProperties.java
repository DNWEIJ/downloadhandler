package com.dwe.springboot.tools.download.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("app.storage")
public record StorageProperties(
    String location
){}