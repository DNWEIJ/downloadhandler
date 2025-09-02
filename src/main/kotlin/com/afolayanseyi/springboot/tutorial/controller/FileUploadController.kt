package com.afolayanseyi.springboot.tutorial.controller

import com.afolayanseyi.springboot.tutorial.exception.StorageFileNotFoundException
import com.afolayanseyi.springboot.tutorial.service.FileStorageService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.IOException
import java.util.*


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

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun handleNotFound(e: StorageFileNotFoundException): ResponseEntity<String> =
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


    @GetMapping("/")
    @Throws(IOException::class)
    fun getFiles(model: Model): String {
        return "uploadForm"
    }
}