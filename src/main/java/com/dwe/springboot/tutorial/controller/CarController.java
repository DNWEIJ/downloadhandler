package com.dwe.springboot.tutorial.controller;

import com.dwe.springboot.tutorial.model.CarEntity;
import com.dwe.springboot.tutorial.service.FileCarStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.NoSuchElementException;

@Controller
class CarController {
    final FileCarStorageService carService;

    CarController(FileCarStorageService carService) {
        this.carService = carService;
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<String> handleNotFound(NoSuchElementException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleNotFound(IllegalArgumentException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/car")
    String saveCarRecord(@ModelAttribute("car") CarEntity car, BindingResult result, ModelMap model) {
        if (car.isValid()) {
            carService.saveRecord(car);
            return "redirect:/car";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/car")
    String getCarRecord(){
        return "car.html";
    }
}