package com.genieLogiciel.Umons.backend.Controller;

import com.genieLogiciel.Umons.backend.auth.LoginRequest;
import com.genieLogiciel.Umons.backend.auth.service.AuthService;
import com.genieLogiciel.Umons.backend.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.backend.model.Student;
import com.genieLogiciel.Umons.backend.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin( "http://localhost:8080")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AuthService authService;

    @PostMapping("/AddStudent")
    public ResponseEntity<String> addStudent(@RequestBody Student newStudent) {
        return studentService.addStudent(newStudent);
    }

    @PostMapping("/students/addCourseCompleted/{matricule}")
    public ResponseEntity<String> addCourseCompleted(@PathVariable Long matricule, @RequestParam String courseName) {
        return studentService.addCourseCompleted(matricule, courseName);
    }

    @PostMapping("/students/login")
    public ResponseEntity<String> login(@RequestBody String email, @RequestBody String password) {
        return studentService.login(email, password);
    }

    @PostMapping("/students/updateOldPAE")
    public ResponseEntity<String> updateOldPAE(@RequestParam Long matricule , @RequestBody Pae pae){
        return studentService.updateOldPAE(matricule,pae);
    }

    @PostMapping("/students/addCoursInActuelPAE/{matricule}")
    public ResponseEntity<String> addCourseInActuelPae(@PathVariable Long matricule , @RequestParam String coursName){
        return studentService.addCoursInActuelPAE(matricule,coursName);
    }

    @DeleteMapping("/students/deleteById")
    public ResponseEntity<String> deleteStudentById(@RequestParam Long matricule) {
        return studentService.deleteStudentById(matricule);
    }

    @DeleteMapping("/students/deleteByName")
    public ResponseEntity<String> deleteStudentByName(@RequestParam String name) {
        return studentService.deleteStudentByName(name);
    }

    @DeleteMapping("/students/deleteAllStudents")
    public ResponseEntity<String> deleteAllStudent() {
        return studentService.deleteAllStudent();
    }

    @GetMapping("/students/getAll")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/students/findById")
    public ResponseEntity<Student> getStudentByMatricule(@RequestParam Long matricule) {
        return studentService.getStudentByMatricule(matricule);
    }

    @GetMapping("/students/getCompletedCourse/")
    public ResponseEntity<List<String>> getCoursesCompleted(@RequestParam Long matricule) {
        return studentService.getCoursReussie(matricule);
    }

    @GetMapping("/student")
    public Student getStudentByEmail(@RequestParam String email , @RequestBody String password){
        return studentService.getStudentByEmail(email , password);
    }

    @PostMapping("/students/getActuelCours")
    public List<Cours> getActuelCours(@RequestBody LoginRequest login){
        String email = login.getEmail();
        String password = login.getPassword();
        return studentService.getActuelCours(email,password);
    }

    @PutMapping("/students/updateFiliere/{matricule}")
    public ResponseEntity<String> updateStudentFiliere(@PathVariable Long matricule, @RequestParam String newFiliere) {
        return studentService.updateStudentFiliere(matricule, newFiliere);
    }

    @PutMapping("/students/updateDepartement/{matricule}")
    public ResponseEntity<String> updateStudentDepartement(@PathVariable Long matricule, @RequestParam String newDepartement) {
        return studentService.updateStudentDepartement(matricule, newDepartement);
    }

    @PutMapping("students/updatePassword/{matricule}")
    public ResponseEntity<String> updateStudentPassword(@PathVariable Long matricule, @RequestParam String newPassword) {
        return studentService.updateStudentPassword(matricule, newPassword);
    }

    @PostMapping("/students/addCourseWithCourseId/{matricule}")
    public ResponseEntity<String> addNewCoursToStudentWithCourseId(@PathVariable Long matricule , @RequestParam Long coursId){
        return studentService.addNewCourseWithCourseId(matricule,coursId);
    }

}
