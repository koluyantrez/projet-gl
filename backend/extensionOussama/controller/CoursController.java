package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.service.CoursService;
import com.genieLogiciel.Umons.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour la gestion des cours.
 */
@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/cours")
public class CoursController {

    @Autowired
    private CoursService coursService;


    /**
     * Ajoute un nouveau cours.
     *
     * @param cours Le nouveau cours à ajouter.
     * @return ResponseEntity contenant le message de succès ou d'erreur.
     */
    @PostMapping("/addNew")
    public ResponseEntity<String> addNewCours(@RequestBody Cours cours) {
        return coursService.addNewCours(cours);
    }

    /**
     * Supprime un cours par son identifiant.
     *
     * @param id L'identifiant du cours à supprimer.
     * @return ResponseEntity contenant le message de succès ou d'erreur.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCoursById(@PathVariable Long id) {
        return coursService.deleteCoursById(id);
    }

    /**
     * Récupère un cours par son nom.
     *
     * @param coursName Le nom du cours à récupérer.
     * @return ResponseEntity contenant le cours ou un message d'erreur.
     */
    @GetMapping("/getByName")
    public ResponseEntity<Cours> getCoursByName(@RequestParam String coursName) {
        return coursService.getCoursByName(coursName);
    }

    /**
     * Supprime un cours par son nom.
     *
     * @param coursName Le nom du cours à supprimer.
     * @return ResponseEntity contenant le message de succès ou d'erreur.
     */
    @DeleteMapping("/deleteByName")
    public ResponseEntity<String> deleteCoursByName(@RequestParam String coursName) {
        return coursService.deleteCoursByName(coursName);
    }

    /**
     * Supprime tous les cours.
     *
     * @return ResponseEntity contenant le message de succès ou d'erreur.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllCours() {
        return coursService.deleteAllCours();
    }

    /**
     * Récupère la liste des étudiants pour un cours donné.
     *
     * @param coursName Le nom du cours.
     * @return ResponseEntity contenant la liste des étudiants ou un message d'erreur.
     */
    @GetMapping("/studentList")
    public ResponseEntity<List<String>> studentListOfThisCours(@RequestParam String coursName) {
        return coursService.studentListForThisCours(coursName);
    }

    /**
     * Récupère la liste de tous les professeurs pour un cours donné.
     *
     * @param coursName Le nom du cours.
     * @return ResponseEntity contenant la liste des professeurs ou un message d'erreur.
     */
    @GetMapping("/teachersList")
    public ResponseEntity<List<String>> listOfAllTeacherForThisCours(@RequestParam String coursName) {
        return coursService.listOfAllTeachersToThisCours(coursName);
    }

    /**
     * Récupère la liste de tous les cours.
     *
     * @return ResponseEntity contenant la liste des cours ou un message d'erreur.
     */
    @GetMapping("/All")
    public ResponseEntity<List<Cours>> getAllCours() {
        return coursService.getAllCours();
    }

}
