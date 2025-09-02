package com.dwe.springboot.tutorial.controller

import com.dwe.springboot.tutorial.exception.StorageFileNotFoundException
import com.dwe.springboot.tutorial.model.CarRecord
import com.dwe.springboot.tutorial.service.FileCarStorageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping


@Controller
class CarController(
    private val carService: FileCarStorageService
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

    @PostMapping("/car")
    fun saveCarRecord(@ModelAttribute("car") car: CarRecord): String {
        if (car.km > 0 && car.kmTotal > 0 && car.person.length > 0 && car.date.length > 0) {
            carService.saveRecord(car)
            return "redirect:/car"
        } else {
            return "redirect:/error"
        }
    }

    @GetMapping("/car")
    fun getCarRecord(): String {
        return "car"
    }
}