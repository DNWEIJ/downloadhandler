package com.dwe.springboot.tutorial.controller;

import com.dwe.springboot.tutorial.model.CarEntity;
import com.dwe.springboot.tutorial.service.FileCarStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
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
    String saveCarRecord(@ModelAttribute("car") CarEntity car) {
        if (car.isValid()) {
            carService.saveRecord(car);
            return "redirect:/success";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/car")
    String getCarRecord(Model model, Authentication authentication) {
        model.addAttribute("person", authentication.getName());
        return "car.html";
    }

    @GetMapping("/success")
    String getSuccess() {
        return "success.html";
    }

    @GetMapping("/car/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getCarRecordList() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/vnd.ms-excel");
        responseHeaders.add("Content-Disposition", "attachment; filename=cars.csv");
        List<String> data = carService.getAll();
        return new ResponseEntity<>(data.stream().reduce((a, b) -> a + "\n" + b).get(), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/car/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCarRecordList() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/vnd.ms-excel");
        responseHeaders.add("Content-Disposition", "attachment; filename=cars.csv");
        List<String> data = carService.getAll();
        carService.deleteCarRecords();
        return new ResponseEntity<>(data.stream().reduce((a, b) -> a + "\n" + b).get(), responseHeaders, HttpStatus.OK);
    }
}