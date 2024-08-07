package com.genieLogiciel.Umons.Controller;

import com.genieLogiciel.Umons.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.model.Filiere;
import com.genieLogiciel.Umons.model.Departement;
import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:8080")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<String> addStudent(@RequestBody Student newStudent) {
        return studentService.addStudent(newStudent);
    }

    @PostMapping("/{matricule}/image")
    public ResponseEntity<String> addImageToStudent(@PathVariable Long matricule, @RequestParam("imageFile") MultipartFile imageFile) {
        return studentService.addImageToStudent(matricule, imageFile);
    }

    @GetMapping("/{matricule}/image")
    public ResponseEntity<byte[]> getImageOfStudent(@PathVariable Long matricule) {
        return studentService.getImageOfStudent(matricule);
    }

    @DeleteMapping("/{matricule}")
    public ResponseEntity<String> deleteStudentById(@PathVariable Long matricule) {
        return studentService.deleteStudentById(matricule);
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<String> deleteStudentByName(@PathVariable String name) {
        return studentService.deleteStudentByName(name);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllStudents() {
        return studentService.deleteAllStudent();
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Student> getStudentByName(@PathVariable String name) {
        return studentService.getStudentByName(name);
    }

    @GetMapping("/{matricule}")
    public ResponseEntity<Student> getStudentByMatricule(@PathVariable Long matricule) {
        return studentService.getStudentByMatricule(matricule);
    }

    @PutMapping("/{matricule}/filiere")
    public ResponseEntity<String> updateStudentFiliere(@PathVariable Long matricule, @RequestParam Filiere newFiliere) {
        return studentService.updateStudentFiliere(matricule, newFiliere);
    }

    @PutMapping("/{matricule}/departement")
    public ResponseEntity<String> updateStudentDepartement(@PathVariable Long matricule, @RequestParam Departement newDepartement) {
        return studentService.updateStudentDepartement(matricule, newDepartement);
    }

    @PutMapping("/{matricule}/password")
    public ResponseEntity<String> updateStudentPassword(@PathVariable Long matricule, @RequestParam String newPassword) {
        return studentService.updateStudentPassword(matricule, newPassword);
    }

    @PutMapping("/{matricule}/courses")
    public ResponseEntity<String> addCourseCompleted(@PathVariable Long matricule, @RequestParam String courseName) {
        return studentService.addCourseCompleted(matricule, courseName);
    }

    @GetMapping("/{matricule}/courses/completed")
    public ResponseEntity<List<String>> getCoursReussie(@PathVariable Long matricule) {
        return studentService.getCoursReussie(matricule);
    }

    @PostMapping("/{matricule}/courses/{coursId}")
    public ResponseEntity<String> addNewCourseWithCourseId(@PathVariable Long matricule, @PathVariable Long coursId) {
        return studentService.addNewCourseWithCourseId(matricule, coursId);
    }

    @GetMapping("/courses")
    public List<Cours> getActuelCours(@RequestParam String email, @RequestParam String password) {
        return studentService.getActuelCours(email, password);
    }

    @PutMapping("/{matricule}/pae")
    public ResponseEntity<String> updateOldPAE(@PathVariable Long matricule, @RequestBody Pae newOldPae) {
        return studentService.updateOldPAE(matricule, newOldPae);
    }

    @PutMapping("/{matricule}/pae/courses")
    public ResponseEntity<String> addCoursInActuelPAE(@PathVariable Long matricule, @RequestParam String coursName) {
        return studentService.addCoursInActuelPAE(matricule, coursName);
    }

    @DeleteMapping("/{matricule}/pae/courses")
    public ResponseEntity<String> removeCoursFromActuelPae(@RequestParam Cours cours, @PathVariable Long matricule) {
        return studentService.removeCoursFromActuelPae(cours, matricule);
    }

}
