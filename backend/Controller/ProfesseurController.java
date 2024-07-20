package com.genieLogiciel.Umons.Controller;

import com.genieLogiciel.Umons.model.Professeur;
import com.genieLogiciel.Umons.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professeurs")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    @Autowired
    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNewTeacher(@RequestBody Professeur newProfesseur) {
        return professeurService.addNewTeacher(newProfesseur);
    }

    @GetMapping("/{matricule}")
    public ResponseEntity<Professeur> getTeacherById(@PathVariable Long matricule) {
        return professeurService.getTeacherById(matricule);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllTeachers() {
        professeurService.deleteAllTeachers();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Professeur>> getAllTeachers() {
        List<Professeur> allTeachers = professeurService.getAllTeachers();
        return ResponseEntity.ok(allTeachers);
    }

    @DeleteMapping("/{matricule}")
    public ResponseEntity<String> deleteTeacherById(@PathVariable Long matricule) {
        return professeurService.deleteTeacherById(matricule);
    }

    @PostMapping("/{matricule}/addFiliere")
    public ResponseEntity<String> addFiliereToTeacher(@PathVariable Long matricule, @RequestBody String sector) {
        return professeurService.addFiliereToTeacher(matricule, sector);
    }

    @GetMapping("/{matricule}/filieres")
    public ResponseEntity<List<String>> getAllFilieresTeacher(@PathVariable Long matricule) {
        return professeurService.getAllFilieresTeacher(matricule);
    }

    @GetMapping("/{matricule}/exists")
    public ResponseEntity<Boolean> existsTeacherById(@PathVariable Long matricule) {
        return ResponseEntity.ok(professeurService.existsTeacherById(matricule));
    }

    @PostMapping("/{matricule}/updatePassword")
    public ResponseEntity<String> updateProfPassword(@PathVariable Long matricule, @RequestBody String newPassword) {
        return professeurService.updateProfPassword(matricule, newPassword);
    }

    @PostMapping("/courses")
    public ResponseEntity<List<String>> getCourses(@RequestBody String email, @RequestBody String password) {
        return professeurService.getCourses(email, password);
    }

    @PostMapping("/{matricule}/attributeCourse")
    public ResponseEntity<String> attributeCourse(@RequestBody String coursName, @PathVariable Long matricule) {
        return professeurService.attributeCourse(coursName, matricule);
    }

    @DeleteMapping("/deleteCourse")
    public ResponseEntity<String> deleteCoursFromProfesseur(@RequestBody String coursName) {
        return professeurService.deleteCoursFromProfesseur(coursName);
    }

    @GetMapping("/findByName")
    public ResponseEntity<Professeur> findProfesseurByName(@RequestParam String teacherName) {
        Professeur professeur = professeurService.findProfesseurByName(teacherName);
        if (professeur != null) {
            return ResponseEntity.ok(professeur);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
