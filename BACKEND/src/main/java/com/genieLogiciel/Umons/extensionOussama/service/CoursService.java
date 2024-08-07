package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.repository.CoursRepository;
import com.genieLogiciel.Umons.model.Professeur;
import com.genieLogiciel.Umons.repository.ProfesseurRepository;
import com.genieLogiciel.Umons.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing course-related operations.
 */
@Service
public class CoursService {

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private ProfesseurService professeurService;

    /**
     * Adds a new course to the repository.
     * Also generates and assigns a unique code to the course and associates it with a teacher if specified.
     *
     * @param newCours The course entity to be added.
     * @return A ResponseEntity containing a message indicating the result of the operation.
     *         If successful and the teacher is found, the status code is OK (200).
     *         If the course already exists, the status code is BAD_REQUEST (400).
     *         If the teacher is not found, the status code is BAD_REQUEST (400).
     */
    @Transactional
    public ResponseEntity<String> addNewCours(Cours newCours) {
        if (coursRepository.existsByName(newCours.getName())) {
            return ResponseEntity.badRequest().body("Course already exists");
        }

        // Save the course in the database before generating the code
        Cours savedCours = coursRepository.save(newCours);

        // Generate and save the course code
        savedCours.setCode("S-" + savedCours.getId());
        coursRepository.save(savedCours);

        System.out.println("The teacher is: " + newCours.getTeacherName());

        if (newCours.getTeacherName() != null) {
            Optional<Professeur> professeurOptional = professeurRepository.findByName(newCours.getTeacherName());
            if (professeurOptional.isPresent()) {
                Professeur professeur = professeurOptional.get();
                // Add the course to the professor's course list
                List<String> courseList = professeur.getCourseList() != null ? professeur.getCourseList() : new ArrayList<>();
                courseList.add(savedCours.getName());
                professeur.setCourseList(courseList);
                professeurRepository.save(professeur);

                return ResponseEntity.ok("Course added and assigned to teacher successfully");
            } else {
                return ResponseEntity.badRequest().body("Teacher not found");
            }
        }

        return ResponseEntity.ok("Course added successfully");
    }

    /**
     * Retrieves a course by its name.
     *
     * @param coursName The name of the course to retrieve.
     * @return A ResponseEntity containing the course if found, otherwise a NOT_FOUND (404) status.
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
     * Deletes a course by its name.
     * Also removes the course from associated professors.
     *
     * @param coursName The name of the course to delete.
     * @return A ResponseEntity containing a message indicating the result of the operation.
     *         If successful, the status code is OK (200).
     *         If the course is not found, the status code is NOT_FOUND (404).
     */
    @Transactional
    public ResponseEntity<String> deleteCoursByName(String coursName) {
        Optional<Cours> coursOptional = coursRepository.findByName(coursName);
        if (coursOptional.isPresent()) {
            Cours cours = coursOptional.get();
            removeCoursFromProfesseurs(cours);
            coursRepository.delete(cours);
            return ResponseEntity.ok("Course deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes all courses from the repository.
     *
     * @return A ResponseEntity containing a message indicating the result of the operation.
     *         The status code is OK (200).
     */
    @Transactional
    public ResponseEntity<String> deleteAllCours() {
        coursRepository.deleteAll();
        return ResponseEntity.ok("All courses deleted successfully");
    }

    /**
     * Retrieves all courses from the repository.
     *
     * @return A ResponseEntity containing a list of all courses.
     */
    public ResponseEntity<List<Cours>> getAllCours() {
        List<Cours> coursList = coursRepository.findAll();
        return ResponseEntity.ok(coursList);
    }

    /**
     * Retrieves the list of students enrolled in a specific course.
     *
     * @param coursName The name of the course.
     * @return A ResponseEntity containing the list of students if the course exists, otherwise a NOT_FOUND (404) status.
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
     * Retrieves the list of all teachers associated with a specific course.
     *
     * @param coursName The name of the course.
     * @return A ResponseEntity containing the list of all teachers if the course exists, otherwise a NOT_FOUND (404) status.
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
     * Deletes a course by its ID.
     * Also removes the course from associated professors.
     *
     * @param id The ID of the course to delete.
     * @return A ResponseEntity containing a message indicating the result of the operation.
     *         If successful, the status code is OK (200).
     *         If the course is not found, the status code is NOT_FOUND (404).
     */
    @Transactional
    public ResponseEntity<String> deleteCoursById(Long id) {
        Optional<Cours> coursOptional = coursRepository.findById(id);
        if (coursOptional.isPresent()) {
            Cours cours = coursOptional.get();
            removeCoursFromProfesseurs(cours);
            coursRepository.delete(cours);
            return ResponseEntity.ok("Course deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Removes a course from the list of courses associated with professors.
     *
     * @param cours The course to remove.
     */
    private void removeCoursFromProfesseurs(Cours cours) {
        List<String> teachers = cours.getListOfAllteachersToThisCours();
        if (teachers != null) {
            for (String teacherName : teachers) {
                Optional<Professeur> professeurOptional = professeurRepository.findByName(teacherName);
                if (professeurOptional.isPresent()) {
                    Professeur professeur = professeurOptional.get();
                    List<String> courseList = professeur.getCourseList();
                    if (courseList != null) {
                        courseList.remove(cours.getName());
                        professeur.setCourseList(courseList);
                        professeurRepository.save(professeur);
                    }
                }
            }
        }
    }
}
