package com.dwe.springboot.tutorial.controller

import com.dwe.springboot.tutorial.service.FileStorageService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.IOException


@Controller
class FileUploadController(
    private val fileStorageService: FileStorageService
) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleNotFound(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(com.dwe.springboot.tutorial.exception.StorageFileNotFoundException::class)
    fun handleNotFound(e: com.dwe.springboot.tutorial.exception.StorageFileNotFoundException): ResponseEntity<String> =
        ResponseEntity.notFound().build()

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createFile(
        @RequestParam("files") files: List<MultipartFile>, redirectAttributes: RedirectAttributes
    ): String {

        files.forEach { file ->
            fileStorageService.saveFile(file)
        }

        return "redirect:/"
    }


    @GetMapping("/upload")
    @Throws(IOException::class)
    fun getFiles(model: Model): String {
        return "uploadForm"
    }
}