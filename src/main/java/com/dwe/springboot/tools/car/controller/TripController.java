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

import java.text.DecimalFormat;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
class TripController {

    String startTable = """
            <table id="table">
            <thead>
            <tr><td>Daniel</td><td>Suzanne</td><td>Maria</td><td>Km</td><td>Liters</td><td>Amount</td></tr>
            </thead>
            <tbody>
            """;
    String startRow = """
            <tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>
            """;
    String endTable = """
            <tfoot></tfoot></table>
            """;

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
        return "trip.html";
    }

    record KmLiter(int km, int liter) {
    }

    @GetMapping("/trip/alluser/tank")
    public String getCarListTank(Model model, Authentication authentication) {
        DecimalFormat df = new DecimalFormat("##.##");

        List<TripEntity> list = driveService.getAllAsList();
        StringBuffer sb = new StringBuffer();
        sb.append(startTable);
        int totalToyota = 0, totalVW = 0, totalT_Daniel = 0, totalT_Maria = 0, totalT_Suzanne = 0, totalVW_Daniel = 0, totalVW_Maria = 0, totalVW_Suzanne = 0;
        int startToyota = 0, startVW = 0;

        for (TripEntity tripEntity : list) {
            if (tripEntity.getCarType().equalsIgnoreCase("Toyota")) {
                if (totalToyota == 0) startToyota = tripEntity.getKmTotal();
                totalToyota = tripEntity.getKmTotal();
                if (tripEntity.getPerson().equalsIgnoreCase("daniel")) {
                    totalT_Daniel += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("maria")) {
                    totalT_Maria += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("suzanne")) {
                    totalT_Suzanne += tripEntity.getKm();
                }
            }

            if (tripEntity.getCarType().equalsIgnoreCase("VW")) {
                if (totalVW == 0) startVW = tripEntity.getKmTotal();
                totalVW = tripEntity.getKmTotal();
                if (tripEntity.getPerson().equalsIgnoreCase("daniel")) {
                    totalVW_Daniel += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("maria")) {
                    totalVW_Maria += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("Suzanne")) {
                    totalVW_Suzanne += tripEntity.getKm();
                }
            }

            if (tripEntity.getLiters() != 0) {
                if (tripEntity.getCarType().equalsIgnoreCase("VW")) {
                    int totalKms = totalVW - startVW;
                    float totalAmount = ((float)tripEntity.getAmount()) / 100;

                    sb.append(startRow.formatted(totalVW_Daniel, totalVW_Suzanne, totalVW_Maria, totalKms, tripEntity.getLiters(),
                            df.format(totalAmount) + " (VW)"));

                    sb.append(startRow.formatted(
                            (totalVW_Daniel == 0) ? "0%" : df.format(totalVW_Daniel * 1.0 / totalKms * 100) + "%",
                            (totalVW_Suzanne == 0) ? "0%" : df.format(totalVW_Suzanne * 1.0 / totalKms * 100) + "%",
                            (totalVW_Maria == 0) ? "0%" : df.format(totalVW_Maria * 1.0 / totalKms * 100) + "%", "", "VW", "percentage"));
                    sb.append(startRow.formatted(
                            (totalVW_Daniel == 0) ? " 0" : df.format(totalVW_Daniel * 1.0 / totalKms * totalAmount),
                            (totalVW_Suzanne == 0) ? "0" : df.format(totalVW_Suzanne * 1.0 / totalKms * totalAmount),
                            (totalVW_Maria == 0) ? "0" : df.format(totalVW_Maria * 1.0 / totalKms * totalAmount), "", "VW", "price")
                    );

                    totalVW = totalVW_Daniel = totalVW_Maria = totalVW_Suzanne = 0;
                }
                if (tripEntity.getCarType().equalsIgnoreCase("Toyota")) {
                    int totalKms = totalToyota - startToyota;
                    float totalAmount = tripEntity.getAmount() / 100;
                    sb.append(startRow.formatted(totalT_Daniel, totalT_Suzanne, totalT_Maria, totalKms, tripEntity.getLiters(),
                            totalAmount + " (Toyota)"));

                    sb.append(startRow.formatted(
                            (totalT_Daniel == 0) ? "0%" : df.format(totalT_Daniel * 1.0 / totalKms * 100) + "%",
                            (totalT_Suzanne == 0) ? "0%" : df.format(totalT_Suzanne * 1.0 / totalKms * 100) + "%",
                            (totalT_Maria == 0) ? "0%" : df.format(totalT_Maria * 1.0 / totalKms * 100) + "%", "", "Toyota", "parts")
                    );
                    sb.append(startRow.formatted(
                            (totalT_Daniel == 0) ? "0%" : df.format(totalT_Daniel * 1.0 / totalKms * 100 * totalAmount),
                            (totalT_Suzanne == 0) ? "0%" : df.format(totalT_Suzanne * 1.0 / totalKms * 100 * totalAmount),
                            (totalT_Maria == 0) ? "0%" : df.format(totalT_Maria * 1.0 / totalKms * 100 * totalAmount), "", "Toyota", "parts")
                    );

                    totalAmount = totalT_Daniel = totalT_Suzanne = totalT_Maria = 0;
                    sb.append(startRow.formatted("", "", "", "", "", ""));
                }
            }
        }
        sb.append(startRow.formatted("left over:", "", "", "", "", ""));
        sb.append(startRow.formatted(totalVW_Daniel, totalVW_Suzanne, totalVW_Maria, "VW", "", ""));
        sb.append(startRow.formatted(totalT_Daniel, totalT_Suzanne, totalT_Maria, "Toyota", "", ""));
        model.addAttribute("fueloverview", sb.append(endTable));
        return "overview.html";
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
        model.addAttribute("imagesrc", gifies[counter++ % 5]);
        return "success.html";
    }

}