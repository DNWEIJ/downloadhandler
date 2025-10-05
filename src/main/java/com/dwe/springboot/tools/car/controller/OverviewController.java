package com.dwe.springboot.tools.car.controller;

import com.dwe.springboot.tools.car.model.TripEntity;
import com.dwe.springboot.tools.car.service.CarService;
import com.dwe.springboot.tools.car.service.DriveService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
class OverviewController {

    // ***************
// plumbing
// ***************
    final DriveService driveService;
    final CarService carService;
    String startTable = """
            <details class="collapsable-table">
            <summary>Click to toggle</summary>
            <table class="table-tight %s" id="table">
            """;
    String headerTable = """
            <thead>
            <tr><td>Daan</td><td>Suus</td><td>Maria&nbsp;&nbsp;&nbsp;</td><td>Tot Km</td><td>Ltrs</td><td>€</td><td>Paid</td></tr>
            </thead>
            """;
    String dataRowTable = """
            <tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>
            """;
    String footerTable = """
            <tfoot></tfoot></table></details>
            """;
    DecimalFormat df = new DecimalFormat("##.##");

    OverviewController(DriveService driveService, CarService carService) {
        this.driveService = driveService;
        this.carService = carService;
    }

    @GetMapping("/trip/alluser/tank")
    public String getCarListTank(Model model) {

        List<TripEntity> list = driveService.getAllAsList();
        StringBuffer sb = new StringBuffer();

        int totalT_Daniel = 0, totalT_Maria = 0, totalT_Suzanne = 0;
        int totalVW_Daniel = 0, totalVW_Maria = 0, totalVW_Suzanne = 0;
        int oddOreven = 0;

        for (TripEntity tripEntity : list) {
            if (tripEntity.getCarType().equalsIgnoreCase("Toyota")) {

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
                if (tripEntity.getPerson().equalsIgnoreCase("daniel")) {
                    totalVW_Daniel += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("maria")) {
                    totalVW_Maria += tripEntity.getKm();
                }
                if (tripEntity.getPerson().equalsIgnoreCase("suzanne")) {
                    totalVW_Suzanne += tripEntity.getKm();
                }
            }

            if (tripEntity.getLiters() != 0) {
                sb.append(startTable.formatted((oddOreven++ % 2 == 0) ? "odd" : "even"));
                sb.append(headerTable);
                sb.append("<tbody>");
                double totalAmount = tripEntity.getAmount() * 1.0 / 100;

                if (tripEntity.getCarType().equalsIgnoreCase("VW")) {
                    int totalKms = totalVW_Daniel + totalVW_Suzanne + totalVW_Maria;

                    sb.append(dataRowTable.formatted(totalVW_Daniel, totalVW_Suzanne, totalVW_Maria, totalKms,
                                    tripEntity.getLiters(),
                                    "€" + df.format(totalAmount),
                                    "<b>" + tripEntity.getPerson() + "</b>"
                            )
                    );

                    extracted(sb,
                            totalVW_Daniel, (totalVW_Daniel * 1.0 / totalKms * 100),
                            totalVW_Suzanne, (totalVW_Suzanne * 1.0 / totalKms * 100),
                            totalVW_Maria, (totalVW_Maria * 1.0 / totalKms * 100),
                            totalAmount);

                    totalVW_Daniel = totalVW_Maria = totalVW_Suzanne = 0;
                }
                if (tripEntity.getCarType().equalsIgnoreCase("Toyota")) {
                    int totalKms = totalT_Daniel + totalT_Suzanne + totalT_Maria;

                    sb.append(dataRowTable.formatted(totalT_Daniel, totalT_Suzanne, totalT_Maria, totalKms,
                                    tripEntity.getLiters(),
                                    "€" + df.format(totalAmount),
                                    "<b>" + tripEntity.getPerson() + "</b>"
                            )
                    );

                    extracted(sb, totalT_Daniel, (totalT_Daniel * 1.0 / totalKms * 100),
                            totalT_Suzanne, (totalT_Suzanne * 1.0 / totalKms * 100),
                            totalT_Maria, (totalT_Maria * 1.0 / totalKms * 100),
                            totalAmount);

                    totalT_Daniel = totalT_Suzanne = totalT_Maria = 0;
                    sb.append(dataRowTable.formatted("", "", "", "", "", "", ""));
                }
                sb.append("</tbody>");
                sb.append(footerTable);
            }
        }
        sb.append(startTable.formatted((oddOreven % 2 == 0) ? "odd" : "even"));
        sb.append(headerTable);
        sb.append(dataRowTable.formatted("left over:", "", "", "", "", "", ""));
        sb.append(dataRowTable.formatted(totalVW_Daniel, totalVW_Suzanne, totalVW_Maria,"", "VW", "", ""));
        sb.append(dataRowTable.formatted(totalT_Daniel, totalT_Suzanne, totalT_Maria,"", "Toyota", "", ""));
        sb.append("</tbody>");
        sb.append(footerTable);

        model.addAttribute("fueloverview", sb.append(footerTable));
        return "overview.html";
    }

    private void extracted(StringBuffer sb, int total_Daniel, double percDaniel, int total_Suzanne, double percSuzanne, int totalT_Maria, double percMaria, double totalAmount) {
        sb.append(dataRowTable.formatted(
                        (total_Daniel == 0) ? "0 %" : df.format(percDaniel) + "%",
                        (total_Suzanne == 0) ? "0 %" : df.format(percSuzanne) + "%",
                        (totalT_Maria == 0) ? "0 %" : df.format(percMaria) + "%",
                        "", "%", "VW", ""
                )
        );
        sb.append(dataRowTable.formatted(
                        (total_Daniel == 0) ? "€0" : "€" + df.format(totalAmount * percDaniel / 100),
                        (total_Suzanne == 0) ? "€0" : "€" + df.format(totalAmount * percSuzanne / 100),
                        (totalT_Maria == 0) ? "€0" : "€" + df.format(totalAmount * percMaria / 100),
                        "", "fuel", "VW", ""
                )
        );
        sb.append(dataRowTable.formatted(
                        (total_Daniel == 0) ? "€0" : "€" + df.format(total_Daniel * 0.1),
                        (total_Suzanne == 0) ? "€0" : "€" + df.format(total_Suzanne * 0.1),
                        (totalT_Maria == 0) ? "€0" : "€" + df.format(totalT_Maria * 0.1),
                        "", "cost", "VW", ""
                )
        );
    }

    @GetMapping("/trip/alluser")
    public String getCarList(Model model) {
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

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<String> handleNotFound(NoSuchElementException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleNotFound(IllegalArgumentException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}