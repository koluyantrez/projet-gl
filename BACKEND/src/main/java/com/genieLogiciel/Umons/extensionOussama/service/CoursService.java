package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.repository.CoursRepository;
import com.genieLogiciel.Umons.model.Professeur;
import com.genieLogiciel.Umons.repository.ProfesseurRepository;
import com.genieLogiciel.Umons.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des cours.
 */
@Service
public class CoursService {

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    @Lazy
    private ProfesseurService professeurService;

    /**
     * Ajoute un nouveau cours.
     *
     * @param newCours Le cours à ajouter.
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    @Transactional
    public ResponseEntity<String> addNewCours(Cours newCours) {
        if (coursRepository.existsByName(newCours.getName())) {
            return ResponseEntity.badRequest().body("Cours already exists");
        }

        // Sauvegarder le cours dans la base de données avant d'ajouter le code
        Cours savedCours = coursRepository.save(newCours);

        // Générer et sauvegarder le code du cours
        savedCours.setCode("S-" + savedCours.getId());
        coursRepository.save(savedCours);
        System.out.println("le teacher est : " + newCours.getTeacherName());
        if (newCours.getTeacherName() != null) {
            Optional<Professeur> professeurOptional = professeurRepository.findByName(newCours.getTeacherName());
            if (professeurOptional.isPresent()) {
                Professeur professeur = professeurOptional.get();
                // Ajouter le cours à la liste des cours du professeur
                List<String> listCours = professeur.getCourseList() != null ? professeur.getCourseList() : new ArrayList<>();
                listCours.add(savedCours.getName());
                professeur.setCourseList(listCours);
                professeurRepository.save(professeur);

                return ResponseEntity.ok("Cours added and assigned to teacher successfully");
            } else {
                return ResponseEntity.badRequest().body("Teacher not found");
            }
        }

        return ResponseEntity.ok("Cours added successfully");
    }

    /**
     * Récupère un cours par son nom.
     *
     * @param coursName Le nom du cours à récupérer.
     * @return ResponseEntity contenant le cours s'il est trouvé, sinon un message d'erreur.
     */
    public ResponseEntity<Cours> getCoursByName(String coursName) {
        Optional<Cours> coursOptional = coursRepository.findByName(coursName);
        if (coursOptional.isPresent()) {
            Cours cours = coursOptional.get();
            return ResponseEntity.ok(cours);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime un cours par son nom.
     *
     * @param coursName Le nom du cours à supprimer.
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    @Transactional
    public ResponseEntity<String> deleteCoursByName(String coursName) {
        Optional<Cours> coursOptional = coursRepository.findByName(coursName);
        if (coursOptional.isPresent()) {
            Cours cours = coursOptional.get();
            removeCoursFromProfesseurs(cours);
            coursRepository.delete(cours);
            return ResponseEntity.ok("Cours deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime tous les cours.
     *
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    @Transactional
    public ResponseEntity<String> deleteAllCours() {
        coursRepository.deleteAll();
        return ResponseEntity.ok("All courses deleted successfully");
    }

    /**
     * Récupère tous les cours.
     *
     * @return ResponseEntity contenant la liste de tous les cours.
     */
    public ResponseEntity<List<Cours>> getAllCours() {
        List<Cours> coursList = coursRepository.findAll();
        return ResponseEntity.ok(coursList);
    }

    /**
     * Récupère la liste des étudiants pour un cours donné.
     *
     * @param coursName Le nom du cours.
     * @return ResponseEntity contenant la liste des étudiants du cours, s'il existe.
     */
    public ResponseEntity<List<String>> studentListForThisCours(String coursName) {
        Optional<Cours> coursOptional = coursRepository.findByName(coursName);
        if (coursOptional.isPresent()) {
            Cours cours = coursOptional.get();
            return ResponseEntity.ok(cours.getStudentList());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère la liste de tous les professeurs pour un cours donné.
     *
     * @param coursName Le nom du cours.
     * @return ResponseEntity contenant la liste de tous les professeurs pour le cours, s'il existe.
     */
    public ResponseEntity<List<String>> listOfAllTeachersToThisCours(String coursName) {
        Optional<Cours> coursOptional = coursRepository.findByName(coursName);
        if (coursOptional.isPresent()) {
            Cours cours = coursOptional.get();
            return ResponseEntity.ok(cours.getListOfAllteachersToThisCours());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime un cours par son ID.
     *
     * @param id L'ID du cours à supprimer.
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    @Transactional
    public ResponseEntity<String> deleteCoursById(Long id) {
        Optional<Cours> coursOptional = coursRepository.findById(id);
        if (coursOptional.isPresent()) {
            Cours cours = coursOptional.get();
            removeCoursFromProfesseurs(cours);
            coursRepository.delete(cours);
            return ResponseEntity.ok("Cours deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime un cours de la liste des cours des professeurs associés.
     *
     * @param cours Le cours à supprimer.
     */
    private void removeCoursFromProfesseurs(Cours cours) {
        List<String> listTeachersToThisCours = cours.getListOfAllteachersToThisCours();
        if (listTeachersToThisCours != null) {
            for (String teacherName : listTeachersToThisCours) {
                Optional<Professeur> professeurOptional = professeurRepository.findByName(teacherName);
                if (professeurOptional.isPresent()) {
                    Professeur professeur = professeurOptional.get();
                    List<String> listCours = professeur.getCourseList();
                    if (listCours != null) {
                        listCours.remove(cours.getName());
                        professeur.setCourseList(listCours);
                        professeurRepository.save(professeur);
                    }
                }
            }
        }
    }
}
