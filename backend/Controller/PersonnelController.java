package com.genieLogiciel.Umons.Controller;

import com.genieLogiciel.Umons.model.Personnel;
import com.genieLogiciel.Umons.service.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel")
@CrossOrigin("http://localhost:8080")
public class PersonnelController {

    @Autowired
    private PersonnelService personnelService;

    @GetMapping("/all")
    public ResponseEntity<List<Personnel>> getAllPersonnel() {
        List<Personnel> personnels = personnelService.getAllPersonnel();
        return ResponseEntity.ok(personnels);
    }

    @GetMapping("/departement/{departement}")
    public ResponseEntity<List<Personnel>> getPersonnelByDepartement(@PathVariable String departement) {
        List<Personnel> personnels = personnelService.getPersonnelByDepartement(departement);
        return ResponseEntity.ok(personnels);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Personnel>> getPersonnelByCategory(@PathVariable String category) {
        List<Personnel> personnels = personnelService.getPersonnelByCategory(category);
        return ResponseEntity.ok(personnels);
    }

    @GetMapping("/matricule/{id}")
    public ResponseEntity<Personnel> getPersonnelById(@PathVariable Long id) {
        return personnelService.getPersonnelById(id);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Personnel> getPersonnelByName(@PathVariable String name) {
        return personnelService.getPersonnelByName(name);
    }

    @DeleteMapping("/delete/{matricule}")
    public ResponseEntity<String> deletePersonnelById(@PathVariable Long matricule) {
        return personnelService.deletePersonnelById(matricule);
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<String> deletePersonnelByName(@PathVariable String name) {
        return personnelService.deletePersonnelByName(name);
    }
}
