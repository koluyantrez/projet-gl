package com.genieLogiciel.Umons.backend.Controller;


import com.genieLogiciel.Umons.backend.model.Personnel;
import com.genieLogiciel.Umons.backend.service.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personnel")
@CrossOrigin("http://localhost:8080")
public class PersonnelController {

    @Autowired
    private PersonnelService personnelService;


    @GetMapping("/all")
    public List<Personnel> getAllPersonnel() {
         return personnelService.getAllPersonnel();
    }

    @GetMapping("/departement/{departement}")
    public List<Personnel> getPersonnelByDepartement(@PathVariable String departement) {
        return personnelService.getPersonnelByDepartement(departement);
    }

    @GetMapping("/category/{category}")
    public List<Personnel> getPersonnelByCategory(@PathVariable String category) {
       return personnelService.getPersonnelByCategory(category);
    }

    @GetMapping("/matricule/{id}")
    public Personnel getPersonnelById(@PathVariable Long id){
        return personnelService.getPersonnelById(id);
    }

    @GetMapping("/name/{name}")
    public Personnel getPersonnelByName(@PathVariable String name){
        return personnelService.getPersonnelByName(name);
    }

    @DeleteMapping("/delete/matricule/{matricule}")
    public ResponseEntity<String> deletePeronnelById(@PathVariable Long matricule){
        return personnelService.deletePersonnelById(matricule);
    }

    @DeleteMapping("/delete/name/{name}")
    public ResponseEntity<String> deletePersonnelByName(@PathVariable String name){
        return personnelService.deletePersonnelByName(name);
    }



}


