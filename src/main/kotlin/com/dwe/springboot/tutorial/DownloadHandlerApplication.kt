package com.dwe.springboot.tutorial

import com.dwe.springboot.tutorial.service.FileCarStorageService
import com.dwe.springboot.tutorial.service.FileStorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Bean


@SpringBootApplication
@ConfigurationPropertiesScan
class DownloadHandlerApplication {

    @Bean
    fun init(storageService: FileStorageService, carService: FileCarStorageService): CommandLineRunner {
        return CommandLineRunner { args: Array<String?>? ->
            storageService.init()
            carService.init()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DownloadHandlerApplication::class.java, *args)
        }
    }
}