package com.dwe.springboot.tutorial;


import com.dwe.springboot.tutorial.service.FileCarStorageService;
import com.dwe.springboot.tutorial.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
class DownloadHandlerApplication implements CommandLineRunner {

    @Autowired
    FileStorageService storageService;
    @Autowired
    FileCarStorageService carService;

    @Override
    public void run(String... args) {
        storageService.init();
        carService.init();
    }

    static void main(String[] args) {
        SpringApplication.run(DownloadHandlerApplication.class, args);
    }
}