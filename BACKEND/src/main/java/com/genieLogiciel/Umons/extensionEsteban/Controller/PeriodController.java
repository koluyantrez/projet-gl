package com.genieLogiciel.Umons.extensionEsteban.Controller;

import com.genieLogiciel.Umons.extensionEsteban.Service.PeriodService;
import com.genieLogiciel.Umons.extensionEsteban.model.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * The type Period controller.
 */
@RestController
@RequestMapping("/api/period")
public class PeriodController {

    @Autowired
    private PeriodService periodService;

    /**
     * Define period response entity.
     *
     * @param period the period
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<String> definePeriod(@RequestBody Period period) {
        periodService.savePeriod(period);
        return ResponseEntity.ok("Période définie avec succès");
    }

    /**
     * Gets current period.
     *
     * @return the current period
     */
    @GetMapping
    public ResponseEntity<Period> getCurrentPeriod() {
        Period period = periodService.getCurrentPeriod();
        return ResponseEntity.ok(period);
    }

//    @PostMapping("/set")
//    public ResponseEntity<Period> setCurrentPeriod(@RequestParam String startDate, @RequestParam String endDate) {
//        LocalDate start = LocalDate.parse(startDate);
//        LocalDate end = LocalDate.parse(endDate);
//        Period period = periodService.setCurrentPeriod(start, end);
//        return ResponseEntity.ok(period);
//    }
}