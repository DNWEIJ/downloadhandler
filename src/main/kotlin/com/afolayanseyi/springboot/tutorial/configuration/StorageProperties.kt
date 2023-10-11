package com.afolayanseyi.springboot.tutorial.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

const val UploadDirectory = "uploadDir"

@ConfigurationProperties("com.afolayanseyi.tutorial.storage")
data class StorageProperties(
    val location: String
)
