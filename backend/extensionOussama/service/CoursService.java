package com.genieLogiciel.Umons.backend.extensionOussama.service;

import com.genieLogiciel.Umons.backend.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.backend.extensionOussama.repository.CoursRepository;
import com.genieLogiciel.Umons.backend.model.Professeur;
import com.genieLogiciel.Umons.backend.repository.ProfesseurRepository;
import com.genieLogiciel.Umons.backend.service.ProfesseurService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProfesseurService professeurService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Ajoute un nouveau cours.
     *
     * @param newCours Le cours à ajouter.
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    @Transactional
    public ResponseEntity<String> addNewCours(Cours newCours) {
        // Vérifier si le cours existe déjà
        boolean coursExists = checkIfCoursExistsByName(newCours.getName());
        if (coursExists) {
            return new ResponseEntity<>("Cours already exists", HttpStatus.BAD_REQUEST);
        }

        // Rechercher le professeur par son nom
        Professeur professeur = professeurService.findProfesseurByName(newCours.getTeacherName());
        if (professeur != null) {
            // Le professeur existe, ajouter le cours à sa liste de cours
            List<String> listOfTeachers = new ArrayList<>(); // Initialisation de la liste
            if (newCours.getListOfAllteachersToThisCours() != null) { // Vérification de nullité
                listOfTeachers.addAll(newCours.getListOfAllteachersToThisCours());
            }
            listOfTeachers.add(professeur.getName());
            newCours.setListOfAllteachersToThisCours(listOfTeachers);
            // Sauvegarder le cours dans la base de données
            coursRepository.save(newCours);
            newCours.setCode("S-" + newCours.getId());
            newCours.setName(newCours.getCode() + " " + newCours.getName());
            coursRepository.save(newCours);
            List<String> listCours = professeur.getCourseList();
            listCours.add(newCours.getName());
            professeur.setCourseList(listCours);
            professeurRepository.save(professeur);//Ajouter ce cours dans la liste des cours de ce professeur
            return new ResponseEntity<>("Cours added successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Teacher not found", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Vérifie si un cours existe déjà dans la base de données.
     *
     * @param coursName Le nom du cours à vérifier.
     * @return true si le cours existe, sinon false.
     */
    private boolean checkIfCoursExistsByName(String coursName) {
        String queryString = "SELECT COUNT(*) FROM Cours c WHERE c.name = :coursName";
        Long count = (Long) entityManager.createQuery(queryString)
                .setParameter("coursName", coursName)
                .getSingleResult();
        return count != 0;
    }

    /**
     * Récupère un cours par son nom.
     *
     * @param coursName Le nom du cours à récupérer.
     * @return ResponseEntity contenant le cours s'il est trouvé, sinon un message d'erreur.
     */
    public ResponseEntity<Cours> getCoursByName(String coursName){
        String queryString = "SELECT c FROM Cours c WHERE c.name = :coursName";
        try{
            Cours cours = (Cours) entityManager.createQuery(queryString)
                    .setParameter("coursName",coursName)
                    .getSingleResult();
            return new ResponseEntity<>(cours,HttpStatus.OK);

        } catch (NoResultException e){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Supprime un cours par son nom.
     *
     * @param coursName Le nom du cours à supprimer.
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    public ResponseEntity<String> deleteCoursByName(String coursName) {
        try {
            String queryString = "DELETE FROM Cours c WHERE c.name = :coursName";
            int deletedCount = entityManager.createQuery(queryString)
                    .setParameter("coursName", coursName)
                    .executeUpdate();
            if (deletedCount > 0) {
                return new ResponseEntity<>("Cours deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Cours not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete cours: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Supprime tous les cours.
     *
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    public ResponseEntity<String> deleteAllCours(){
        coursRepository.deleteAll();
        return new ResponseEntity<>("delete success" , HttpStatus.OK);
    }

    /**
     * Récupère tous les cours.
     *
     * @return ResponseEntity contenant la liste de tous les cours.
     */
    public ResponseEntity<List<Cours>> getAllCours(){
        List<Cours> coursList = coursRepository.findAll();
        return new ResponseEntity<>(coursList , HttpStatus.OK);
    }

    /**
     * Récupère la liste des étudiants pour un cours donné.
     *
     * @param coursName Le nom du cours.
     * @return ResponseEntity contenant la liste des étudiants du cours, s'il existe.
     */
    public ResponseEntity<List<String>> studentListForThisCours(String  coursName){
        String queryString = "SELECT c FROM Cours c WHERE c.name = :coursName";
        try{
            Cours cours = (Cours) entityManager.createQuery(queryString)
                    .setParameter("coursName",coursName)
                    .getSingleResult();
            return new ResponseEntity<>(cours.getStudentList(),HttpStatus.OK);

        } catch (NoResultException e){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Récupère la liste de tous les professeurs pour un cours donné.
     *
     * @param coursName Le nom du cours.
     * @return ResponseEntity contenant la liste de tous les professeurs pour le cours, s'il existe.
     */
    public ResponseEntity<List<String>> listOfAllTeachersToThisCours(String coursName){
        String queryString = "SELECT c FROM Cours c WHERE c.name = :coursName";
        try{
            Cours cours = (Cours) entityManager.createQuery(queryString)
                    .setParameter("coursName",coursName)
                    .getSingleResult();
            return new ResponseEntity<>(cours.getListOfAllteachersToThisCours(),HttpStatus.OK);

        } catch (NoResultException e){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Supprime un cours par son ID.
     *
     * @param id L'ID du cours à supprimer.
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    public ResponseEntity<String> deleteCoursById(Long id) {
        Optional<Cours> coursOptional = coursRepository.findById(id);
        if (coursOptional.isPresent()) {
            Cours cours = coursOptional.get();
            List<String> listTeachersToThisCours = cours.getListOfAllteachersToThisCours();

            for (String prf : listTeachersToThisCours) {
                Professeur professeur = professeurService.findProfesseurByName(prf);
                System.out.println("Le nom du professeur de ce cours est : " + professeur.getName());
                if (professeur != null) {
                    professeurRepository.deleteCourseFromProfessors(cours.getName());
                    System.out.println("delete success from listCours");
                }
            }

            coursRepository.delete(cours);
            return new ResponseEntity<>("Delete success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cours not found", HttpStatus.BAD_REQUEST);
        }
    }
}
