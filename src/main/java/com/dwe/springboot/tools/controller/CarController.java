package com.dwe.springboot.tools.controller;

import com.dwe.springboot.tools.model.CarEntity;
import com.dwe.springboot.tools.service.FileCarStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        model.addAttribute("role", ((User) authentication.getPrincipal()).getAuthorities().toString());
    }

    @PostMapping("/car")
    String saveCarRecord(@ModelAttribute("car") CarEntity car, RedirectAttributes redirect) {
        if (car.isValid()) {
            Long id = carService.saveRecord(car);
            redirect.addFlashAttribute("successAction", carService.getHtmlStringOf(id));
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


    @GetMapping("/car/alluser")
    public String getCarList(Model model, Authentication authentication) {
        List<CarEntity> list;
        if (
                authentication.getAuthorities()
                        .contains(new SimpleGrantedAuthority("ROLE_ADMIN")
                        )
        ) {
            list = carService.getAllAsList();
        } else {
            list = carService.getAllAsList(authentication.getName());
        }
        model.addAttribute("cars", list);
        model.addAttribute("kmTotal",
                list.stream()
                        .map(CarEntity::getKm).map(Long::valueOf)
                        .reduce(0L, Long::sum)
        );
        return "listcars.html";
    }

    @GetMapping("/car/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getCarRecordList() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/csv");
        responseHeaders.add("Content-Disposition", "attachment; filename=cars.csv");
        List<String> data = carService.getAllAsCsv();
        return new ResponseEntity<>(data.stream().reduce((a, b) -> a + "\n" + b).get(), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/car/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCarRecordList() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/csv");
        responseHeaders.add("Content-Disposition", "attachment; filename=cars.csv");
        List<String> data = carService.getAllAsCsv();
        carService.deleteCarRecords();
        return new ResponseEntity<>(data.stream().reduce((a, b) -> a + "\n" + b).get(), responseHeaders, HttpStatus.OK);
    }
}