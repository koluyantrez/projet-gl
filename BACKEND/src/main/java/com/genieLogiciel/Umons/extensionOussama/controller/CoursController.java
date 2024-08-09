package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.service.CoursService;
import com.genieLogiciel.Umons.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for course management.
 * This controller provides REST endpoints to perform CRUD operations on courses,
 * as well as to retrieve specific information related to courses, such as the list of students
 * and teachers associated with a given course.
 */
@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/cours")
public class CoursController {

    @Autowired
    private CoursService coursService;

    @Autowired
    private ProfesseurRepository professeurRepository;

    /**
     * Adds a new course.
     *
     * @param cours The new course to be added.
     * @return ResponseEntity containing the success or error message.
     */
    @PostMapping("/addNew")
    public ResponseEntity<String> addNewCours(@RequestBody Cours cours) {
        return coursService.addNewCours(cours);
    }

    /**
     * Deletes a course by its identifier.
     *
     * @param id The identifier of the course to be deleted.
     * @return ResponseEntity containing the success or error message.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCoursById(@PathVariable Long id) {
        return coursService.deleteCoursById(id);
    }

    /**
     * Retrieves a course by its name.
     *
     * @param coursName The name of the course to be retrieved.
     * @return ResponseEntity containing the course or an error message.
     */
    @GetMapping("/getByName")
    public ResponseEntity<Cours> getCoursByName(@RequestParam String coursName) {
        Cours cours = coursService.getCoursByName(coursName).getBody();
        if (cours != null) {
            return ResponseEntity.ok(cours);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a course by its name.
     *
     * @param coursName The name of the course to be deleted.
     * @return ResponseEntity containing the success or error message.
     */
    @DeleteMapping("/deleteByName")
    public ResponseEntity<String> deleteCoursByName(@RequestParam String coursName) {
        return coursService.deleteCoursByName(coursName);
    }

    /**
     * Deletes all courses.
     *
     * @return ResponseEntity containing the success or error message.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllCours() {
        return coursService.deleteAllCours();
    }

    /**
     * Retrieves the list of students for a given course.
     *
     * @param coursName The name of the course.
     * @return ResponseEntity containing the list of students or an error message.
     */
    @GetMapping("/studentList")
    public ResponseEntity<List<String>> studentListOfThisCours(@RequestParam String coursName) {
        return coursService.studentListForThisCours(coursName);
    }

    /**
     * Retrieves the list of all teachers for a given course.
     *
     * @param coursName The name of the course.
     * @return ResponseEntity containing the list of teachers or an error message.
     */
    @GetMapping("/teachersList")
    public ResponseEntity<List<String>> listOfAllTeacherForThisCours(@RequestParam String coursName) {
        return coursService.listOfAllTeachersToThisCours(coursName);
    }

    /**
     * Retrieves the list of all courses.
     *
     * @return ResponseEntity containing the list of courses or an error message.
     */
    @GetMapping("/All")
    public ResponseEntity<List<Cours>> getAllCours() {
        return coursService.getAllCours();
    }



    @PostMapping("/{coursId}/prerequis/{preRequisId}")
    public ResponseEntity<Cours> addPreRequis(@PathVariable Long coursId, @PathVariable Long preRequisId) {
        Cours updatedCours = coursService.addPreRequis(coursId, preRequisId);
        return updatedCours != null ? ResponseEntity.ok(updatedCours) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{coursId}/prerequis/{preRequisId}")
    public ResponseEntity<Cours> removePreRequis(@PathVariable Long coursId, @PathVariable Long preRequisId) {
        Cours updatedCours = coursService.removePreRequis(coursId, preRequisId);
        return updatedCours != null ? ResponseEntity.ok(updatedCours) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{coursId}/prerequis")
    public ResponseEntity<List<Cours>> getPreRequis(@PathVariable Long coursId) {
        List<Cours> preRequis = coursService.getPreRequis(coursId);
        return preRequis != null ? ResponseEntity.ok(preRequis) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{coursId}/corequis/{coRequisId}")
    public ResponseEntity<Cours> addCoRequis(@PathVariable Long coursId, @PathVariable Long coRequisId) {
        Cours updatedCours = coursService.addCoRequis(coursId, coRequisId);
        return updatedCours != null ? ResponseEntity.ok(updatedCours) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{coursId}/corequis/{coRequisId}")
    public ResponseEntity<Cours> removeCoRequis(@PathVariable Long coursId, @PathVariable Long coRequisId) {
        Cours updatedCours = coursService.removeCoRequis(coursId, coRequisId);
        return updatedCours != null ? ResponseEntity.ok(updatedCours) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{coursId}/corequis")
    public ResponseEntity<List<Cours>> getCoRequis(@PathVariable Long coursId) {
        List<Cours> coRequis = coursService.getCoRequis(coursId);
        return coRequis != null ? ResponseEntity.ok(coRequis) : ResponseEntity.notFound().build();
    }
}