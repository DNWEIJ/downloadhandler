package com.dwe.springboot.tools.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void init();
    void saveFile(MultipartFile file);

}