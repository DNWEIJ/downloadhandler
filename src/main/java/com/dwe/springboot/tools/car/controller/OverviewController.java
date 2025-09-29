package com.dwe.springboot.tools.car.controller;

import com.dwe.springboot.tools.car.model.TripEntity;
import com.dwe.springboot.tools.car.service.CarService;
import com.dwe.springboot.tools.car.service.DriveService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
class OverviewController {

    String startTable = """
            <table class="table-tight" id="table">
            <thead>
            <tr><td>Daniel</td><td>Suzanne</td><td>Maria</td><td>Km</td><td>Liters</td><td>Amount</td></tr>
            </thead>
            """;
    String startRow = """
            <tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>
            """;
    String endTable = """
            <tfoot></tfoot></table>
            """;

    record KmLiter(int km, int liter) {
    }

    @GetMapping("/trip/alluser/tank")
    public String getCarListTank(Model model, Authentication authentication) {
        DecimalFormat df = new DecimalFormat("##.##");

        List<TripEntity> list = driveService.getAllAsList();
        StringBuffer sb = new StringBuffer();
        sb.append(startTable);
        int totalT_Daniel = 0, totalT_Maria = 0, totalT_Suzanne = 0;
        int totalVW_Daniel = 0, totalVW_Maria = 0, totalVW_Suzanne = 0;
        int startToyota = 0, startVW = 0;

        for (TripEntity tripEntity : list) {
            if (tripEntity.getCarType().equalsIgnoreCase("Toyota")) {
                if (startToyota == 0) startToyota = tripEntity.getKmTotal();
                if (tripEntity.getPerson().equalsIgnoreCase("Daniel")) {
                    totalT_Daniel += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("Maria&nbsp;")) {
                    totalT_Maria += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("Suzanne")) {
                    totalT_Suzanne += tripEntity.getKm();
                }
            }

            if (tripEntity.getCarType().equalsIgnoreCase("VW")) {
                if (startVW == 0) startVW = tripEntity.getKmTotal();
                if (tripEntity.getPerson().equalsIgnoreCase("Daniel")) {
                    totalVW_Daniel += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("Maria&nbsp;")) {
                    totalVW_Maria += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("Suzanne")) {
                    totalVW_Suzanne += tripEntity.getKm();
                }
            }

            if (tripEntity.getLiters() != 0) {
                sb.append("<tbody>");
                double totalAmount = tripEntity.getAmount() * 1.0 / 100;

                if (tripEntity.getCarType().equalsIgnoreCase("VW")) {
                    int totalKms = tripEntity.getKmTotal() - startVW;

                    sb.append(startRow.formatted(totalVW_Daniel, totalVW_Suzanne, totalVW_Maria, totalKms,
                            tripEntity.getLiters(), df.format(totalAmount) + " <b>(" + tripEntity.getPerson() + ")</b>"));

                    double percDaniel = (totalVW_Daniel * 1.0 / totalKms * 100);
                    double percSuzanne = (totalVW_Suzanne * 1.0 / totalKms * 100);
                    double percMaria = (totalVW_Maria * 1.0 / totalKms * 100);

                    sb.append(startRow.formatted(
                            (totalVW_Daniel == 0) ? "0 %" : df.format(percDaniel) + " %",
                            (totalVW_Suzanne == 0) ? "0 %" : df.format(percSuzanne) + " %",
                            (totalVW_Maria == 0) ? "0 %" : df.format(percMaria) + " %", "", "VW", " %"));
                    sb.append(startRow.formatted(
                            (totalVW_Daniel == 0) ? "€ 0" : df.format(totalAmount * percDaniel),
                            (totalVW_Suzanne == 0) ? "€ 0" : df.format(totalAmount * percSuzanne),
                            (totalVW_Maria == 0) ? "€ 0" : df.format(totalAmount * percMaria), "", "VW", "fuel")
                    );
                    sb.append(startRow.formatted(
                            (totalVW_Daniel == 0) ? "€ 0" : "€ " + df.format(totalVW_Daniel * 0.1),
                            (totalVW_Suzanne == 0) ? "€ 0" : "€ " + df.format(totalVW_Suzanne * 0.1),
                            (totalVW_Maria == 0) ? "€ 0" : "€ " + df.format(totalVW_Maria * 0.1), "", "VW", "cost")
                    );

                    startVW = totalVW_Daniel = totalVW_Maria = totalVW_Suzanne = 0;
                }
                if (tripEntity.getCarType().equalsIgnoreCase("Toyota")) {
                    int totalKms = tripEntity.getKmTotal() - startToyota;

                    sb.append(startRow.formatted(totalT_Daniel, totalT_Suzanne, totalT_Maria, totalKms,
                            tripEntity.getLiters(), totalAmount + " <b>(" + tripEntity.getPerson() + ")</b>"));

                    double percDaniel = (totalT_Daniel * 1.0 / totalKms * 100);
                    double percSuzanne = (totalT_Suzanne * 1.0 / totalKms * 100);
                    double percMaria = (totalT_Maria * 1.0 / totalKms * 100);

                    sb.append(startRow.formatted(
                            (totalT_Daniel == 0) ? "0 %" : df.format(percDaniel) + " %",
                            (totalT_Suzanne == 0) ? "0 %" : df.format(percSuzanne) + " %",
                            (totalT_Maria == 0) ? "0 %" : df.format(percMaria) + " %", "", "Toyota", " %")
                    );
                    sb.append(startRow.formatted(
                            (totalT_Daniel == 0) ? "€ 0" : "€ " + df.format(totalAmount * percDaniel / 100),
                            (totalT_Suzanne == 0) ? "€ 0" : "€ " + df.format(totalAmount * percSuzanne / 100),
                            (totalT_Maria == 0) ? "€ 0" : "€ " + df.format(totalAmount * percMaria / 100), "", "Toyota", "fuel")
                    );
                    sb.append(startRow.formatted(
                            (totalT_Daniel == 0) ? "€ 0" : "€ " + df.format(totalT_Daniel * 0.1),
                            (totalT_Suzanne == 0) ? "€ 0" : "€ " + df.format(totalT_Suzanne * 0.1),
                            (totalT_Maria == 0) ? "€ 0" : "€ " + df.format(totalT_Maria * 0.1), "", "Toyota", "cost")
                    );


                    startToyota = totalT_Daniel = totalT_Suzanne = totalT_Maria = 0;
                    sb.append(startRow.formatted("", "", "", "", "", ""));
                }
                sb.append("</tbody>");
            }
        }
        sb.append("<tbody>");
        sb.append(startRow.formatted("left over:", "", "", "", "", ""));
        sb.append(startRow.formatted(totalVW_Daniel, totalVW_Suzanne, totalVW_Maria, "VW", "", ""));
        sb.append(startRow.formatted(totalT_Daniel, totalT_Suzanne, totalT_Maria, "Toyota", "", ""));
        sb.append("</tbody>");

        model.addAttribute("fueloverview", sb.append(endTable));
        return "overview.html";
    }

    @GetMapping("/trip/alluser")
    public String getCarList(Model model, Authentication authentication) {
        List<TripEntity> list = driveService.getAllAsList();

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

    OverviewController(DriveService driveService, CarService carService) {
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
}