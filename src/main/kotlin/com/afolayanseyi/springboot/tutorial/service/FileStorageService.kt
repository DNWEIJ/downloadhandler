package com.afolayanseyi.springboot.tutorial.service

import org.springframework.web.multipart.MultipartFile

interface FileStorageService {
    fun init()
    fun saveFile(file: MultipartFile): String

}