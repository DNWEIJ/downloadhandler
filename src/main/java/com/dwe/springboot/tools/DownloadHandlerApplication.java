package com.dwe.springboot.tools;


import com.dwe.springboot.tools.service.FileCarStorageService;
import com.dwe.springboot.tools.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableMethodSecurity
class DownloadHandlerApplication implements CommandLineRunner {

    @Autowired
    FileStorageService storageService;
    @Autowired
    FileCarStorageService carService;

    static void main(String[] args) {
        SpringApplication.run(DownloadHandlerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        storageService.init();
        carService.init();
    }
}