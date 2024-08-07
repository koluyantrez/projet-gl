package com.genieLogiciel.Umons.Controller;

import com.genieLogiciel.Umons.model.Category;
import com.genieLogiciel.Umons.model.Departement;
import com.genieLogiciel.Umons.model.Personnel;
import com.genieLogiciel.Umons.service.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personnel")
@CrossOrigin("http://localhost:8080")
public class PersonnelController {

    @Autowired
    private PersonnelService personnelService;

    @GetMapping
    public ResponseEntity<List<Personnel>> searchPersonnel(@RequestParam(required = false, defaultValue = "") String query) {
        List<Personnel> personnels = personnelService.searchPersonnel(query);
        return ResponseEntity.ok(personnels);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Personnel>> getAllPersonnel() {
        List<Personnel> personnels = personnelService.getAllPersonnel();
        return ResponseEntity.ok(personnels);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        List<String> departments = Arrays.stream(Departement.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Personnel>> getPersonnelByDepartmentAndCategory(
            @RequestParam(required = false) Departement department,
            @RequestParam(required = false) Category category) {

        List<Personnel> personnels = personnelService.getPersonnelByDepartmentAndCategory(department, category);
        return ResponseEntity.ok(personnels);
    }

    @GetMapping("/departement/{departement}")
    public List<Personnel> getPersonnelByDepartement(@PathVariable Departement departement) {
        return personnelService.getPersonnelByDepartement(departement);
    }

    @GetMapping("/category/{category}")
    public List<Personnel>getPersonnelByCategory(@PathVariable String category) {
        return personnelService.getPersonnelByCategory(category);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = Arrays.stream(Category.values())
                .map(Category::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
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
