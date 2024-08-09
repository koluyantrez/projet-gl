package com.genieLogiciel.Umons.extensionEsteban.Controller;

import com.genieLogiciel.Umons.extensionEsteban.Service.PeriodService;
import com.genieLogiciel.Umons.extensionEsteban.model.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/period")
public class PeriodController {

    @Autowired
    private PeriodService periodService;

    @PostMapping
    public ResponseEntity<String> definePeriod(@RequestBody Period period) {
        periodService.savePeriod(period);
        return ResponseEntity.ok("Période définie avec succès");
    }

    @GetMapping
    public ResponseEntity<Period> getCurrentPeriod() {
        Period period = periodService.getCurrentPeriod();
        return ResponseEntity.ok(period);
    }
}