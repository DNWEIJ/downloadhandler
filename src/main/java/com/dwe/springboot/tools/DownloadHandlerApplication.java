package com.dwe.springboot.tools;


import com.dwe.springboot.tools.car.MigrationService;
import com.dwe.springboot.tools.download.service.FileStorageService;
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
    MigrationService migrationService;

    public static void main(String[] args) {
        SpringApplication.run(DownloadHandlerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        migrationService.init();
        // storageService.init();

    }
}