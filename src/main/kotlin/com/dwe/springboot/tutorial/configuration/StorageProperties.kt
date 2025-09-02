package com.dwe.springboot.tutorial.configuration

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("app.storage")
data class StorageProperties(
    val location: String
)