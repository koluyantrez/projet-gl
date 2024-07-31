package com.genieLogiciel.Umons.service;

import com.genieLogiciel.Umons.auth.service.AbstractLoginService;
import com.genieLogiciel.Umons.auth.service.AuthService;
import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.repository.CoursRepository;
import com.genieLogiciel.Umons.extensionOussama.service.CoursService;
import com.genieLogiciel.Umons.model.Category;
import com.genieLogiciel.Umons.auth.service.EmailDomain;
import com.genieLogiciel.Umons.model.Professeur;
import com.genieLogiciel.Umons.repository.PersonnelRepository;
import com.genieLogiciel.Umons.repository.ProfesseurRepository;
import com.genieLogiciel.Umons.repository.UserRepository;
import com.genieLogiciel.Umons.util.PersonnelMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProfesseurService extends AbstractLoginService {

    private final ProfesseurRepository professeurRepository;
    private final CoursService coursService;
    private final CoursRepository coursRepository;
    private final PersonnelRepository personnelRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    private final PersonnelMapper personnelMapper;
    @Autowired
    public ProfesseurService(AuthService authService, UserService userService, ProfesseurRepository professeurRepository,
                             CoursService coursService, CoursRepository coursRepository, PersonnelRepository personnelRepository, UserRepository userRepository, PersonnelMapper personnelMapper, PersonnelMapper personnelMapper1) {
        super(authService);
        this.professeurRepository = professeurRepository;
        this.coursService = coursService;
        this.coursRepository = coursRepository;
        this.personnelRepository = personnelRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.personnelMapper = personnelMapper1;
    }

    public ResponseEntity<String> addNewTeacher(Professeur newProfesseur) {
        ResponseEntity<String> response = userService.addUser(newProfesseur, EmailDomain.PROFESSOR);
        if (response.getStatusCode() == HttpStatus.OK) {
            professeurRepository.save(newProfesseur);
            personnelMapper.mapToPersonnel(newProfesseur);
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return new ResponseEntity<>("Teacher already exists", HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    public ResponseEntity<Professeur> getTeacherById(Long matricule) {
        return professeurRepository.findById(matricule)
                .map(professeur -> new ResponseEntity<>(professeur, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    @Transactional
    public void deleteAllTeachers() {
        professeurRepository.deleteAll();
        personnelRepository.deleteByCategorie(Category.PROFESSEUR);
    }

    public List<Professeur> getAllTeachers() {
        return professeurRepository.findAll();
    }

    public ResponseEntity<String> deleteTeacherById(Long matricule) {
        Optional<Professeur> professeurOptional = professeurRepository.findById(matricule);
        if (professeurOptional.isPresent()) {
            professeurRepository.deleteById(matricule);
            personnelRepository.findById(matricule).ifPresent(personnelRepository::delete);
            userRepository.findById(matricule).ifPresent(userRepository :: delete);
            return new ResponseEntity<>("Delete success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Teacher not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addFiliereToTeacher(Long matricule, String sector) {
        return professeurRepository.findById(matricule)
                .map(professeur -> {
                    if (professeur.getFilieres().contains(sector)) {
                        return new ResponseEntity<>("Teacher already has this sector", HttpStatus.BAD_REQUEST);
                    }
                    professeur.getFilieres().add(sector);
                    professeurRepository.save(professeur);
                    return new ResponseEntity<>("Sector added successfully", HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>("Teacher not found", HttpStatus.BAD_REQUEST));
    }

    public ResponseEntity<List<String>> getAllFilieresTeacher(Long matricule) {
        return professeurRepository.findById(matricule)
                .map(professeur -> new ResponseEntity<>(professeur.getFilieres(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }

    public Boolean existsTeacherById(Long matricule) {
        return professeurRepository.existsById(matricule);
    }

    public ResponseEntity<String> updateProfPassword(Long matricule, String newPassword) {
        if (newPassword.length() < 15) {
            return new ResponseEntity<>("The password must be at least 15 characters long", HttpStatus.BAD_REQUEST);
        }
        return professeurRepository.findById(matricule)
                .map(professeur -> {
                    professeur.setPassword(newPassword);
                    professeurRepository.save(professeur);
                    return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>("Teacher not found", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<String>> getCourses(String email, String password) {
        Long matricule = Long.valueOf(authService.extractMatriculeFromEmail(email));
        return professeurRepository.findById(matricule)
                .filter(professeur -> professeur.getPassword().equals(password))
                .map(professeur -> new ResponseEntity<>(professeur.getCourseList(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> attributeCourse(String coursName, Long matricule) {
        Optional<Professeur> professeurOptional = professeurRepository.findById(matricule);
        if (professeurOptional.isPresent()) {
            Professeur professeur = professeurOptional.get();
            Optional<Cours> coursResponse = coursRepository.findByName(coursName);
            if (coursResponse.isPresent()) {
                Cours cours = coursResponse.get();
                if (!professeur.getCourseList().contains(coursName)) {
                    professeur.getCourseList().add(coursName);
                    professeurRepository.save(professeur);
                } else {
                    return new ResponseEntity<>("Teacher already has this course", HttpStatus.BAD_REQUEST);
                }
                if (!cours.getListOfAllteachersToThisCours().contains(professeur.getName())) {
                    cours.getListOfAllteachersToThisCours().add(professeur.getName());
                    coursRepository.save(cours);
                }
                return new ResponseEntity<>("Attribute success", HttpStatus.OK);
            }
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Teacher not found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deleteCoursFromProfesseur(String coursName) {
        List<String> affectedProfessors = professeurRepository.deleteCourseFromProfessors(coursName);
        coursRepository.deleteProfesseurFromCourse(coursName, affectedProfessors);
        return new ResponseEntity<>("Delete success", HttpStatus.OK);
    }

    public Professeur findProfesseurByName(String teacherName) {
        return professeurRepository.findByName(teacherName)
                .orElseThrow(() -> new EntityNotFoundException("Professeur not found with name: " + teacherName));
    }


    @Override
    protected ResponseEntity<String> authenticate(Long matricule, String password) {
        return professeurRepository.findById(matricule)
                .map(professeur -> {
                    if (professeur.getPassword().equals(password)) {
                        return ResponseEntity.ok("Login successful");
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
                    }
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found"));
    }

    public void uploadProfesseurImage(Long matricule, MultipartFile file) {
        userService.uploadUserImage(matricule, file); // Utiliser la méthode générique
    }

    public ResponseEntity<String> getProfesseurImage(Long matricule) {
        return userService.getUserImage(matricule); // Utiliser la méthode générique
    }

}
