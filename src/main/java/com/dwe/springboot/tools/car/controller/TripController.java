package com.dwe.springboot.tools.car.controller;

import com.dwe.springboot.tools.car.model.TripEntity;
import com.dwe.springboot.tools.car.service.CarService;
import com.dwe.springboot.tools.car.service.DriveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
class TripController {


    private final String[] gifies = {
            "https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExbjZzNnl4Z2t6NGxuZnRkandheDB0NjhtbWVvazR3OWd2MWZhbTZwNiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/XreQmk7ETCak0/giphy.gif",
            "https://media4.giphy.com/media/v1.Y2lkPTc5MGI3NjExZTJxcHllcDcwejRvYmFza2xxNHE5NTBwOGl2eWVmdjNsNm53bHhpcCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/7kO9VZjCv3FkI/giphy.gif",
            "https://media0.giphy.com/media/v1.Y2lkPTc5MGI3NjExZ2lyOXlpbTJnd3Y0Y2VoeTc5d3NibTY2NXBrZjVmb3l5c3I4aWhjbCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/1GTZA4flUzQI0/giphy.gif",
            "https://media0.giphy.com/media/v1.Y2lkPTc5MGI3NjExNzR0cTQwdzE2bWdnbDQzeWYzaXJ1Y2N2ZnBqdHJqeHFzYXJjMHViNCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/CvZuv5m5cKl8c/giphy.gif",
            "https://media2.giphy.com/media/v1.Y2lkPTc5MGI3NjExbzV3MHEwZWt6eG5oOTY0ODJpd2FheTJvMWFnbHVoaDhwbnNxODU2eiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/8xSnw21AM7OQo/giphy.gif"
    };
    private int counter = 0;

    @PostMapping("/trip")
    String saveCarRecord(@ModelAttribute("car") TripEntity drive, RedirectAttributes redirect) {
        if (drive.isValid()) {
            Long id = driveService.saveRecord(drive);
            carService.saveRecord(drive);

            redirect.addFlashAttribute("successAction", driveService.getHtmlStringOf(id));

            return "redirect:/success";
        } else {
            return "redirect:/error";
        }
    }


    @GetMapping("/trip")
    String getCarRecord(Model model) {
        model.addAttribute("carTypes", carService.getAllNames());
        model.addAttribute("carsPreviousTotal", carService.getAllNameAndTotalKm());
        return "/car/trip.html";
    }


    // ***************
// plumbing
// ***************
    final DriveService driveService;
    final CarService carService;

    TripController(DriveService driveService, CarService carService) {
        this.driveService = driveService;
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

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        model.addAttribute("person", authentication.getName());
        model.addAttribute("role", ((User) authentication.getPrincipal()).getAuthorities().toString());
    }

    @GetMapping("/success")
    String getSuccess(Model model) {
        model.addAttribute("imagesrc", gifies[counter++ % 5]);
        return "success.html";
    }
}