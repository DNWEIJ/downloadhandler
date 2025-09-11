package com.dwe.springboot.tools.car.controller;

import com.dwe.springboot.tools.car.model.TripEntity;
import com.dwe.springboot.tools.car.service.CarService;
import com.dwe.springboot.tools.car.service.DriveService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
class TripController {

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
        return "trip.html";
    }

    @GetMapping("/trip/alluser")
    public String getCarList(Model model, Authentication authentication) {
        List<TripEntity> list;
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            list = driveService.getAllAsList();
        } else {
            list = driveService.getAllAsList(authentication.getName());
        }
        model.addAttribute("trips", list);
        model.addAttribute("kmTotal",
                list.stream()
                        .map(TripEntity::getKm).map(Long::valueOf)
                        .reduce(0L, Long::sum)
        );
        model.addAttribute("litersTotal",
                list.stream()
                        .map(TripEntity::getLiters).map(Long::valueOf)
                        .reduce(0L, Long::sum)
        );
        return "listtrips.html";
    }

    @GetMapping("/trip/all")
    public ResponseEntity<String> getCarRecordList() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/csv");
        responseHeaders.add("Content-Disposition", "attachment; filename=trips.csv");
        return new ResponseEntity<>(
                driveService.getAllAsCsv().stream().reduce((a, b) -> a + "\r\n" + b).get(),
                responseHeaders,
                HttpStatus.OK);
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
        model.addAttribute("imagesrc",
                "https://media3.giphy.com/media/v1.Y2lkPTc5MGI3NjExczF4d2NnbGRodGNyeGNjdXR4NHg4cGtkMGJzYXZwbjBsam9kYzM1NiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/Q81NcsY6YxK7jxnr4v/giphy.gif"
        );
        return "success.html";
    }

}