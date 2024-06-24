package com.genieLogiciel.Umons.backend.service;

import com.genieLogiciel.Umons.backend.auth.service.AuthService;
import com.genieLogiciel.Umons.backend.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.backend.extensionOussama.model.Assistant;
import com.genieLogiciel.Umons.backend.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.backend.extensionOussama.repository.CoursRepository;
import com.genieLogiciel.Umons.backend.extensionOussama.service.CoursService;
import com.genieLogiciel.Umons.backend.model.Student;
import com.genieLogiciel.Umons.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Student service.
 */
@Service
@CrossOrigin("http://localhost:8080")
public class StudentService {


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private CoursService coursService;

    @Autowired
    private AuthService authService;

    /**
     * Add student response entity.
     *
     * @param newStudent the new student
     * @return the response entity
     */
    public ResponseEntity<String> addStudent(Student newStudent) {
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            if (student.getFirstName().equals(newStudent.getFirstName()) && student.getLastName().equals(newStudent.getLastName())) {
                return new ResponseEntity<>("Student already exists.", HttpStatus.BAD_REQUEST);
            }
        }
        newStudent.setName(newStudent.getFirstName() + " " + newStudent.getLastName());
        newStudent.setPassword(generatePassword());
        studentRepository.save(newStudent);
        newStudent.setEmail(newStudent.getMatricule() + "@Illumis.student.ac.be");
        studentRepository.save(newStudent);
        return new ResponseEntity<>("Student added successfully.", HttpStatus.OK);
    }

    /**
     * Delete student by id response entity.
     *
     * @param matricule the matricule
     * @return the response entity
     */
    public ResponseEntity<String> deleteStudentById(Long matricule) {
        Optional<Student> student = studentRepository.findById(matricule);
        if (student.isPresent()) {
            studentRepository.delete(student.get());
            return new ResponseEntity<>("Delete success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Student doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Delete student by name response entity.
     *
     * @param name the name
     * @return the response entity
     */
    public ResponseEntity<String> deleteStudentByName(String name) {
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            if (student.getName().equals(name)) {
                studentRepository.delete(student);
                return new ResponseEntity<>("Delete success", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Student doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Delete all student response entity.
     *
     * @return the response entity
     */
    public ResponseEntity<String> deleteAllStudent() {
        studentRepository.deleteAll();
        return new ResponseEntity<>("Delete success", HttpStatus.OK);
    }

    /**
     * Gets all students.
     *
     * @return the all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Get student by name response entity.
     *
     * @param name the name
     * @return the response entity
     */
    public ResponseEntity<Student> getStudentByName(String name){
        List<Student> students = studentRepository.findAll();
        for (Student student : students){
            if (student.getName().equals(name)){
                return new ResponseEntity<>(student, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
    }

    /**
     * Gets student by matricule.
     *
     * @param matricule the matricule
     * @return the student by matricule
     */
    public ResponseEntity<Student> getStudentByMatricule(Long matricule) {
        Optional<Student> student = studentRepository.findById(matricule);
        if (student.isPresent()) {
            return new ResponseEntity<>(student.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Update student filiere response entity.
     *
     * @param matricule  the matricule
     * @param newFiliere the new filiere
     * @return the response entity
     */
    public ResponseEntity<String> updateStudentFiliere(Long matricule, String newFiliere) {
        Optional<Student> student = studentRepository.findById(matricule);
        if (student.isPresent()) {
            student.get().setFiliere(newFiliere);
            studentRepository.save(student.get());
            return new ResponseEntity<>("Success update", HttpStatus.OK);
        }
        return new ResponseEntity<>("Student doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Update student departement response entity.
     *
     * @param matricule      the matricule
     * @param newDepartement the new departement
     * @return the response entity
     */
    public ResponseEntity<String> updateStudentDepartement(Long matricule, String newDepartement) {
        Optional<Student> student = studentRepository.findById(matricule);
        if (student.isPresent()) {
            student.get().setDepartement(newDepartement);
            studentRepository.save(student.get());
            return new ResponseEntity<>("Success update", HttpStatus.OK);
        }
        return new ResponseEntity<>("Student doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Update student password response entity.
     *
     * @param matricule   the matricule
     * @param newPassword the new password
     * @return the response entity
     */
    public ResponseEntity<String> updateStudentPassword(Long matricule, String newPassword) {
        Optional<Student> student = studentRepository.findById(matricule);
        if (student.isPresent()) {
            if (newPassword.length() < 15) {
                return new ResponseEntity<>("The password must be at least 15 characters long", HttpStatus.BAD_REQUEST);
            }
            student.get().setPassword(newPassword);
            studentRepository.save(student.get());
            return new ResponseEntity<>("Success update of password", HttpStatus.OK);
        }
        return new ResponseEntity<>("Student doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Add course completed response entity.
     *
     * @param matricule  the matricule
     * @param courseName the course name
     * @return the response entity
     */
    public ResponseEntity<String> addCourseCompleted(Long matricule, String courseName) {
        Optional<Student> student = studentRepository.findById(matricule);
        if (student.isPresent()) {
            Student std = student.get();
            List<String> coursesCompleted = std.getCoursReussie();
            if (coursesCompleted.contains(courseName)){
                return new ResponseEntity<>("student have already this cours" , HttpStatus.BAD_REQUEST);
            }
            coursesCompleted.add(courseName);
            studentRepository.save(std);
            return new ResponseEntity<>("Course added successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Student doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Gets cours reussie.
     *
     * @param matricule the matricule
     * @return the cours reussie
     */
    public ResponseEntity<List<String>> getCoursReussie(Long matricule) {
        Optional<Student> student = studentRepository.findById(matricule);
        if (student.isPresent()) {
            Student std = student.get();
            List<String> coursesCompleted = std.getCoursReussie();
            return new ResponseEntity<>(coursesCompleted, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Login response entity.
     *
     * @param email    the email
     * @param password the password
     * @return the response entity
     */
    public ResponseEntity<String> login(String email, String password) {
        String matriculeStr = authService.extractMatriculeFromEmail(email);
        System.out.println("Dans la methode login student : " );
        System.out.println("email = "+ email);
        System.out.println("password = " + password);
        System.out.println("Matricule str =  "+ matriculeStr);
        if (matriculeStr == null) {
            return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
        }

        try {
            Long matricule = Long.parseLong(matriculeStr);
            Optional<Student> studentOptional = studentRepository.findById(matricule);
            if (studentOptional.isPresent()) {
                Student student = studentOptional.get();
                if (student.getPassword().equals(password)) {
                    return new ResponseEntity<>("Login successful", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid matricule format", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> addNewCourseWithCourseId(Long matricule , Long coursId){
        Optional<Student> studentOptional = studentRepository.findById(matricule);
        if (studentOptional.isPresent()){
            Student student = studentOptional.get();
            Optional<Cours> coursOptional = coursRepository.findById(coursId);
            if (coursOptional.isPresent()){
                Cours cours = coursOptional.get();
                if (student.getListOfActuelCours().contains(cours)) {
                    return new ResponseEntity<>("student have already this cours" , HttpStatus.OK);
                }
                student.getListOfActuelCours().add(cours);
                cours.getStudentList().add(student.getName());
                coursRepository.save(cours);
                studentRepository.save(student);
                return new ResponseEntity<>("Cours added in succes" , HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Cours not found" , HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("student not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Get actuel cours response entity.
     *
     * @param email the email
     * @param password the password
     * @return the response entity
     */
    public List<Cours> getActuelCours(String  email , String password){
        Long matricule = Long.valueOf(authService.extractMatriculeFromEmail(email));
        Optional<Student> studentOptional = studentRepository.findById(matricule);
        if (studentOptional.isPresent()){
            Student student = studentOptional.get();
            if (student.getPassword().equals(password)){
                return student.getListOfActuelCours();
            }
            System.out.println("password invalid");
            return null;
        }
        System.out.println("student not found");
        return null;
    }

    /**
     * Update old pae response entity.
     *
     * @param matricule the matricule
     * @param newOldPae the new old pae
     * @return the response entity
     */
    public ResponseEntity<String> updateOldPAE(Long matricule , Pae newOldPae){ //ajouter un pae au autres anciens pae
        Optional<Student> studentOptional = studentRepository.findById(matricule);
        if (studentOptional.isPresent()){
            Student student = studentOptional.get();
            List<Pae> listOfPae = student.getOldPAE();
            listOfPae.add(newOldPae);
            student.setOldPAE(listOfPae);
            studentRepository.save(student);
            return new ResponseEntity<>("oldPae updated successfly" , HttpStatus.OK);
        }
        return new ResponseEntity<>("Student not Found" , HttpStatus.NOT_FOUND);
    }

    /**
     * Add cours in actuel pae response entity.
     *
     * @param matricule the matricule
     * @param coursName     the cours
     * @return the response entity
     */
    public ResponseEntity<String> addCoursInActuelPAE(Long matricule, String coursName) {
        Optional<Student> studentOptional = studentRepository.findById(matricule);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            List<Cours> listOfActuelCours = student.getListOfActuelCours();

            // Vérifie si le cours existe déjà pour l'étudiant
            if (listOfActuelCours.stream().anyMatch(c -> c.getName().equals(coursName))) {
                return new ResponseEntity<>("Course already exists for the student", HttpStatus.BAD_REQUEST);
            }

            // Récupère le cours par son nom
            ResponseEntity<Cours> responseEntity = coursService.getCoursByName(coursName);

            // Vérifie le statut de la réponse
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Cours cours = responseEntity.getBody();
                listOfActuelCours.add(cours);
                student.setListOfActuelCours(listOfActuelCours);
                studentRepository.save(student);
                assert cours != null;
                List<String> studentList = cours.getStudentList();
                studentList.add(student.getName());
                coursRepository.save(cours);
                return new ResponseEntity<>("Course added successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error adding course: Course not found", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
    }


    /**
     * Remove cours from actuel pae response entity.
     *
     * @param cours     the cours
     * @param matricule the matricule
     * @return the response entity
     */
    public ResponseEntity<String> removeCoursFromActuelPae(Cours cours , Long matricule){
        Optional<Student> studentOptional = studentRepository.findById(matricule);
        if (studentOptional.isPresent()){
            Student student = studentOptional.get();
            List<Cours> listOfCours = student.getListOfActuelCours();
            if (listOfCours.contains(cours)){
                listOfCours.remove(cours);
                student.setListOfActuelCours(listOfCours);
                studentRepository.save(student);
                return new ResponseEntity<>("delete cours from list of actuel cours success" , HttpStatus.OK);
            }
            return new ResponseEntity<>("student does not have this cours" , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("student not found" , HttpStatus.NOT_FOUND);
    }


    /**
     * Extract matricule from email string.
     *
     * @param email the email
     * @return the string
     */



    /**
     * Generate password string.
     *
     * @return the string
     */
    public String generatePassword() {
        // Définir les caractères autorisés pour le mot de passe
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_@";

        // Générer le mot de passe aléatoire
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

    public Student getStudentByEmail(String email , String password){
        Long matriculeStr = Long.valueOf(authService.extractMatriculeFromEmail(email));
        Optional<Student> studentOptional = studentRepository.findById(matriculeStr);
        if (studentOptional.isPresent()){
            Student student = studentOptional.get();
            if (!(student.getPassword().equals(password))){
                System.out.println("password invalide");
                return null;
            }
            return student;
        }
        return null;
    }



}
