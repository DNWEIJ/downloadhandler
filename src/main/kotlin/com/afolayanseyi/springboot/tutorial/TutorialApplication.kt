package com.afolayanseyi.springboot.tutorial

import com.afolayanseyi.springboot.tutorial.configuration.StorageProperties
import com.afolayanseyi.springboot.tutorial.service.FileStorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class TutorialApplication {

    @Bean
    fun init(storageService: FileStorageService): CommandLineRunner {
        return CommandLineRunner { args: Array<String?>? ->
            storageService.deleteAll()
            storageService.init()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(TutorialApplication::class.java, *args)
        }
    }
}
