package com.dwe.springboot.tutorial.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void init();
    String saveFile(MultipartFile file);

}