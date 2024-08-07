package com.genieLogiciel.Umons.service;

import com.genieLogiciel.Umons.auth.service.AbstractLoginService;
import com.genieLogiciel.Umons.auth.service.AuthService;
import com.genieLogiciel.Umons.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.model.Filiere;
import com.genieLogiciel.Umons.extensionOussama.repository.CoursRepository;
import com.genieLogiciel.Umons.extensionOussama.service.CoursService;
import com.genieLogiciel.Umons.auth.service.EmailDomain;
import com.genieLogiciel.Umons.model.Departement;
import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.repository.StudentRepository;
import com.genieLogiciel.Umons.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@CrossOrigin("http://localhost:8080")
public class StudentService extends AbstractLoginService {


    private final StudentRepository studentRepository;
    private final  CoursRepository coursRepository;
    private final CoursService coursService;
    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public StudentService(AuthService authService, StudentRepository studentRepository, CoursRepository coursRepository, CoursService coursService, AuthService authService1, UserService userService, UserRepository userRepository) {
        super(authService);
        this.studentRepository = studentRepository;
        this.coursRepository = coursRepository;
        this.coursService = coursService;
        this.authService = authService1;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // Méthodes utilitaires privées
    private Optional<Student> findStudentByMatricule(Long matricule) {
        return studentRepository.findById(matricule);
    }

    private ResponseEntity<String> handleStudentNotFound() {
        return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
    }

    // Gestion des étudiants
    public ResponseEntity<String> addStudent(Student newStudent) {
        ResponseEntity<String> response = userService.addUser(newStudent, EmailDomain.STUDENT);
        if (response.getStatusCode() == HttpStatus.OK) {
            studentRepository.save(newStudent); // Sauvegarde spécifique à l'étudiant dans sa propre table
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return new ResponseEntity<>("Student already exists.", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    public ResponseEntity<String> addImageToStudent(Long matricule, MultipartFile imageFile) {
        return findStudentByMatricule(matricule)
                .map(student -> {
                    try {
                        student.setImage(imageFile.getBytes());
                        studentRepository.save(student);
                        return new ResponseEntity<>("Image added successfully.", HttpStatus.OK);
                    } catch (IOException e) {
                        return new ResponseEntity<>("Error processing image file.", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }).orElseGet(this::handleStudentNotFound);
    }

    public ResponseEntity<byte[]> getImageOfStudent(Long matricule) {
        return findStudentByMatricule(matricule)
                .map(student -> new ResponseEntity<>(student.getImage(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> deleteStudentById(Long matricule) {
        return findStudentByMatricule(matricule)
                .map(student -> {
                    studentRepository.delete(student);
                    userRepository.deleteById(matricule);
                    return new ResponseEntity<>("Delete success", HttpStatus.OK);
                }).orElseGet(this::handleStudentNotFound);
    }

    public ResponseEntity<String> deleteStudentByName(String name) {
        return studentRepository.findByName(name)
                .map(student -> {
                    studentRepository.delete(student);
                    return new ResponseEntity<>("Delete success", HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>("Student doesn't exist", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> deleteAllStudent() {
        studentRepository.deleteAll();
        return new ResponseEntity<>("Delete success", HttpStatus.OK);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public ResponseEntity<Student> getStudentByName(String name) {
        return studentRepository.findByName(name)
                .map(student -> new ResponseEntity<>(student, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Student> getStudentByMatricule(Long matricule) {
        return findStudentByMatricule(matricule)
                .map(student -> new ResponseEntity<>(student, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> updateStudentFiliere(Long matricule, Filiere newFiliere) {
        return findStudentByMatricule(matricule)
                .map(student -> {
                    student.setFiliere(newFiliere);
                    studentRepository.save(student);
                    return new ResponseEntity<>("Success update", HttpStatus.OK);
                }).orElseGet(this::handleStudentNotFound);
    }

    public ResponseEntity<String> updateStudentDepartement(Long matricule, Departement newDepartement) {
        return findStudentByMatricule(matricule)
                .map(student -> {
                    student.setDepartement(newDepartement);
                    studentRepository.save(student);
                    return new ResponseEntity<>("Success update", HttpStatus.OK);
                }).orElseGet(this::handleStudentNotFound);
    }

    public ResponseEntity<String> updateStudentPassword(Long matricule, String newPassword) {
        if (newPassword.length() < 15) {
            return new ResponseEntity<>("The password must be at least 15 characters long", HttpStatus.BAD_REQUEST);
        }
        return findStudentByMatricule(matricule)
                .map(student -> {
                    student.setPassword(newPassword);
                    studentRepository.save(student);
                    return new ResponseEntity<>("Success update of password", HttpStatus.OK);
                }).orElseGet(this::handleStudentNotFound);
    }

    public ResponseEntity<String> addCourseCompleted(Long matricule, String courseName) {
        return findStudentByMatricule(matricule)
                .map(student -> {
                    List<String> coursesCompleted = student.getCoursReussie();
                    if (coursesCompleted.contains(courseName)) {
                        return new ResponseEntity<>("Student already has this course", HttpStatus.BAD_REQUEST);
                    }
                    coursesCompleted.add(courseName);
                    studentRepository.save(student);
                    return new ResponseEntity<>("Course added successfully.", HttpStatus.OK);
                }).orElseGet(this::handleStudentNotFound);
    }

    public ResponseEntity<List<String>> getCoursReussie(Long matricule) {
        return findStudentByMatricule(matricule)
                .map(student -> new ResponseEntity<>(student.getCoursReussie(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> addNewCourseWithCourseId(Long matricule, Long coursId) {
        return findStudentByMatricule(matricule)
                .map(student -> {
                    Optional<Cours> coursOptional = coursRepository.findById(coursId);
                    if (coursOptional.isPresent()) {
                        Cours cours = coursOptional.get();
                        if (student.getListOfActuelCours().contains(cours)) {
                            return new ResponseEntity<>("Student already has this course", HttpStatus.BAD_REQUEST);
                        }
                        student.getListOfActuelCours().add(cours);
                        cours.getStudentList().add(student.getName());
                        coursRepository.save(cours);
                        studentRepository.save(student);
                        return new ResponseEntity<>("Course added successfully", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Course not found", HttpStatus.BAD_REQUEST);
                    }
                }).orElseGet(this::handleStudentNotFound);
    }

    public List<Cours> getActuelCours(String email, String password) {
        Long matricule = Long.valueOf(authService.extractMatriculeFromEmail(email));
        return findStudentByMatricule(matricule)
                .filter(student -> student.getPassword().equals(password))
                .map(Student::getListOfActuelCours)
                .orElse(null);
    }

    public ResponseEntity<String> updateOldPAE(Long matricule, Pae newOldPae) {
        return findStudentByMatricule(matricule)
                .map(student -> {
                    student.getOldPAE().add(newOldPae);
                    studentRepository.save(student);
                    return new ResponseEntity<>("Old PAE updated successfully", HttpStatus.OK);
                }).orElseGet(this::handleStudentNotFound);
    }

    public ResponseEntity<String> addCoursInActuelPAE(Long matricule, String coursName) {
        return findStudentByMatricule(matricule)
                .map(student -> {
                    List<Cours> listOfActuelCours = student.getListOfActuelCours();
                    if (listOfActuelCours.stream().anyMatch(c -> c.getName().equals(coursName))) {
                        return new ResponseEntity<>("Course already exists for the student", HttpStatus.BAD_REQUEST);
                    }
                    ResponseEntity<Cours> responseEntity = coursService.getCoursByName(coursName);
                    if (responseEntity.getStatusCode() == HttpStatus.OK) {
                        Cours cours = responseEntity.getBody();
                        listOfActuelCours.add(cours);
                        student.setListOfActuelCours(listOfActuelCours);
                        studentRepository.save(student);
                        if (cours != null) {
                            cours.getStudentList().add(student.getName());
                            coursRepository.save(cours);
                        }
                        return new ResponseEntity<>("Course added successfully", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
                    }
                }).orElseGet(this::handleStudentNotFound);
    }

    public ResponseEntity<String> removeCoursFromActuelPae(Cours cours, Long matricule) {
        return findStudentByMatricule(matricule)
                .map(student -> {
                    List<Cours> listOfCours = student.getListOfActuelCours();
                    if (listOfCours.contains(cours)) {
                        listOfCours.remove(cours);
                        student.setListOfActuelCours(listOfCours);
                        studentRepository.save(student);
                        return new ResponseEntity<>("Course removed from current PAE successfully", HttpStatus.OK);
                    }
                    return new ResponseEntity<>("Student does not have this course", HttpStatus.BAD_REQUEST);
                }).orElseGet(this::handleStudentNotFound);
    }

    public Student getStudentByEmail(String email, String password) {
        Long matricule = Long.valueOf(authService.extractMatriculeFromEmail(email));
        return findStudentByMatricule(matricule)
                .filter(student -> student.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    protected ResponseEntity<String> authenticate(Long matricule, String password) {
        System.out.println("dans auth stud mat + pass : "+ matricule+password);
        return studentRepository.findById(matricule)
                .map(student -> {
                    if (student.getPassword().equals(password)) {
                        System.out.println("pass student : " + password);
                        return ResponseEntity.ok("Login successful"); // Connexion réussie
                    } else {
                        System.out.println("non aut");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"); // Non autorisé
                    }
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found")); // Non trouvé
    }

}
