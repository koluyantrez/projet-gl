package com.genieLogiciel.Umons.backend.Controller;


import com.genieLogiciel.Umons.backend.auth.LoginRequest;
import com.genieLogiciel.Umons.backend.model.Professeur;
import com.genieLogiciel.Umons.backend.service.ProfesseurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    public ProfesseurController(ProfesseurService professeurService) {
        this.professeurService = professeurService;
    }


    @PostMapping("/AddTeacher")
    public ResponseEntity<String> addNewTeacher(@RequestBody Professeur newProfesseur) {
        return professeurService.addNewTeacher(newProfesseur);
    }

    @GetMapping("/teachers/findById")
    public ResponseEntity<Professeur> getTeacherById(@RequestParam Long matricule) {
        return professeurService.getTeacherById(matricule);
    }

    @GetMapping("/teachers/findByName")
    public ResponseEntity<Professeur> getTeacherByName(@RequestParam String name) {
        return professeurService.getTeacherByName(name);
    }

    @PutMapping("teachers/updatePassword/{matricule}")
    public ResponseEntity<String> updateStudentPassword(@PathVariable Long matricule, @RequestParam String newPassword) {
        return professeurService.updateProfPassword(matricule, newPassword);
    }

    @DeleteMapping("/teachers/deleteAllTeachers")
    public void deleteAllTeachers() {
        professeurService.deleteAllTeachers();
    }

    @GetMapping("/teachers/getAll")
    public List<Professeur> getAllTeachers() {
        return professeurService.getAllteachers();
    }

    @DeleteMapping("/teachers/deleteById")
    public ResponseEntity<String> deleteTeacherById(@RequestParam Long matricule) {
        return professeurService.deleteTeacherById(matricule);
    }

    @PostMapping("/teachers/addFiliere/{matricule}")
    public ResponseEntity<String> addFiliereTeacher(@PathVariable Long matricule, @RequestParam String sector) {
        return professeurService.addFiliereToTeacher(matricule, sector);
    }

    @GetMapping("/teachers/getAllFilieres/{matricule}")
    public ResponseEntity<String> getAllFilieresTeacher(@PathVariable Long matricule) {
        return professeurService.getAllFilieresteacher(matricule);
    }

    @GetMapping("/teachers/verifyTeacherById/{matricule}")
    public Boolean existTeacherById(@PathVariable Long matricule) {
        return professeurService.existTeacherById(matricule);
    }

    @PostMapping("/teachers/getCourses")
    public ResponseEntity<List<String>> getCourses(@RequestBody LoginRequest login){
        String email = login.getEmail();
        String password = login.getPassword();
        return professeurService.getCourses(email,password);
    }


    @DeleteMapping("/teachers/deleteCoursFromProf")
    public ResponseEntity<String> deleteCoursFromProfesseur(@RequestParam String coursName){
        return professeurService.deleteCoursFromProfesseur(coursName);
    }

    @PostMapping("/assign-course{id}")
    public ResponseEntity<String> assignCourseToTeacher(@PathVariable Long id , @RequestParam String coursName) {
        return professeurService.attributeCourse(coursName, id);
    }


}
