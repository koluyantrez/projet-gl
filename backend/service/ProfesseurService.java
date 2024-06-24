package com.genieLogiciel.Umons.backend.service;

import com.genieLogiciel.Umons.backend.auth.service.AuthService;
import com.genieLogiciel.Umons.backend.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.backend.extensionOussama.repository.CoursRepository;
import com.genieLogiciel.Umons.backend.extensionOussama.service.CoursService;
import com.genieLogiciel.Umons.backend.model.Category;
import com.genieLogiciel.Umons.backend.model.Personnel;
import com.genieLogiciel.Umons.backend.model.Professeur;
import com.genieLogiciel.Umons.backend.repository.PersonnelRepository;
import com.genieLogiciel.Umons.backend.repository.ProfesseurRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service pour gérer les opérations liées aux professeurs.
 */
@Service
public class ProfesseurService {

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private CoursService coursService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private AuthService authService;

    /**
     * Ajoute un nouveau professeur.
     *
     * @param newProfesseur Le nouveau professeur à ajouter.
     * @return ResponseEntity contenant un message indiquant le succès ou l'échec de l'ajout.
     */
    public ResponseEntity<String> addNewTeacher(Professeur newProfesseur){
        List<Professeur> professeurs = professeurRepository.findAll();
        for (Professeur professeur : professeurs) {
            if (professeur.getFirstName().equals(newProfesseur.getFirstName()) && professeur.getLastName().equals(newProfesseur.getLastName())) {
                return new ResponseEntity<>("Teacher already exists.", HttpStatus.BAD_REQUEST);
            }
        }
        newProfesseur.setName(newProfesseur.getFirstName() + " " + newProfesseur.getLastName());
        newProfesseur.setPassword(generatePassword());
        professeurRepository.save(newProfesseur);
        newProfesseur.setEmail(newProfesseur.getMatricule() + "@Illumis.professeur.ac.be");
        professeurRepository.save(newProfesseur);

        Personnel newPersonnel = new Personnel();
        newPersonnel.setDepartement(newProfesseur.getDepartement());
        newPersonnel.setMatricule(newProfesseur.getMatricule());
        newPersonnel.setName(newProfesseur.getName());
        newPersonnel.setAdresse(newProfesseur.getAdresse());
        newPersonnel.setNumero(newProfesseur.getNumero());
        List<String> listFilieres = new ArrayList<>(); // Initialisation de la liste
        if (newProfesseur.getFilieres() != null) { // Vérification de nullité
            listFilieres.addAll(newProfesseur.getFilieres());
        }
        newPersonnel.setFilieres(listFilieres);
        newPersonnel.setEmail(newProfesseur.getEmail());
        newPersonnel.setCategorie(Category.PROFESSEUR);
        personnelRepository.save(newPersonnel);

        return new ResponseEntity<>("teacher added successfully.", HttpStatus.OK);
    }

    /**
     * Récupère un professeur par identifiant.
     *
     * @param matricule L'identifiant du professeur à récupérer.
     * @return ResponseEntity contenant le professeur correspondant à l'identifiant spécifié.
     */
    public ResponseEntity<Professeur> getTeacherById(Long matricule){
        Optional<Professeur> professeurOptional = professeurRepository.findById(matricule);
        if (professeurOptional.isPresent()){
            Professeur professeur = professeurOptional.get();
            return new ResponseEntity<>(professeur , HttpStatus.OK);
        }
        return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
    }

    /**
     * Authentifie un professeur.
     *
     * @param email    L'email du professeur.
     * @param password Le mot de passe du professeur.
     * @return ResponseEntity contenant un message indiquant le succès ou l'échec de l'authentification.
     */
    public ResponseEntity<String> login(String email, String password) {
        String matriculeStr = authService.extractMatriculeFromEmail(email);
        System.out.println("Dans la methode login professeur : " );
        System.out.println("email = "+ email);
        System.out.println("password = " + password);
        System.out.println("Matricule str =  "+ matriculeStr);
        if (matriculeStr == null) {
            return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
        }

        try {
            Long matricule = Long.parseLong(matriculeStr);
            Optional<Professeur> professeurOptional = professeurRepository.findById(matricule);
            if (professeurOptional.isPresent()) {
                Professeur professeur = professeurOptional.get();
                if (professeur.getPassword().equals(password)) {
                    return new ResponseEntity<>("Login successful", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>("Teacher not found", HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid matricule format", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Supprime tous les professeurs.
     */
    @Transactional
    public void deleteAllTeachers() {
        professeurRepository.deleteAll();

        // Supprimer tous les professeurs de la liste des personnels en utilisant une requête SQL native
        String sql = "DELETE FROM Personnel WHERE categorie = 'PROFESSEUR'";
        entityManager.createNativeQuery(sql).executeUpdate();

        new ResponseEntity<>("Delete All teachers", HttpStatus.OK);
    }

    /**
     * Récupère tous les professeurs.
     *
     * @return La liste de tous les professeurs.
     */
    public List<Professeur> getAllteachers(){
        return professeurRepository.findAll();
    }

    /**
     * Supprime un professeur par identifiant.
     *
     * @param matricule L'identifiant du professeur à supprimer.
     * @return ResponseEntity contenant un message indiquant le succès ou l'échec de la suppression.
     */
    public ResponseEntity<String> deleteTeacherById(Long matricule) {
        Optional<Professeur> professeurOptional = professeurRepository.findById(matricule);
        if (professeurOptional.isPresent()) {
            Professeur professeur = professeurOptional.get();
            // Vérifier si le professeur existe dans la liste des personnels
            Optional<Personnel> personnelOptional = personnelRepository.findById(matricule);
            if (personnelOptional.isPresent()) {
                Personnel personnel = personnelOptional.get();
                personnelRepository.delete(personnel); // Supprimer le professeur de la liste des personnels
            }
            professeurRepository.deleteById(matricule); // Supprimer le professeur de la base de données des professeurs
            return new ResponseEntity<>("delete success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Teacher not found", HttpStatus.BAD_REQUEST);
    }

    /**
     * Ajoute une filière à un professeur.
     *
     * @param matricule L'identifiant du professeur.
     * @param sector    La filière à ajouter.
     * @return ResponseEntity contenant un message indiquant le succès ou l'échec de l'ajout.
     */
    public ResponseEntity<String> addFiliereToTeacher(Long matricule , String sector){
        Optional<Professeur> professeurOptional = professeurRepository.findById(matricule);
        if (professeurOptional.isPresent()){
            Professeur professeur = professeurOptional.get();
            List<String> listOfAllSectors = professeur.getFilieres();
            if (listOfAllSectors.contains(sector)){
                return new ResponseEntity<>("teacher have already this sector" , HttpStatus.BAD_REQUEST);
            }
            listOfAllSectors.add(sector);
            professeur.setFilieres(listOfAllSectors);
            professeurRepository.save(professeur);
            return new ResponseEntity<>("sector added with success", HttpStatus.OK);
        }
        return new ResponseEntity<>("teacher not found" , HttpStatus.BAD_REQUEST);
    }

    /**
     * Récupère toutes les filières d'un professeur.
     *
     * @param matricule L'identifiant du professeur.
     * @return ResponseEntity contenant la liste des filières du professeur.
     */
    public ResponseEntity<List<String>> getAllFilieresteacher(Long matricule){
        Optional<Professeur> professeurOptional = professeurRepository.findById(matricule);
        if (professeurOptional.isPresent()){
            Professeur professeur = professeurOptional.get();
            List<String> listOfSectors = professeur.getFilieres();
            return new ResponseEntity<>(listOfSectors , HttpStatus.OK);
        }
        return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
    }

    /**
     * Vérifie l'existence d'un professeur par identifiant.
     *
     * @param matricule L'identifiant du professeur.
     * @return true si le professeur existe, sinon false.
     */
    public Boolean existTeacherById(Long matricule){
        return professeurRepository.existsById(matricule);
    }

    /**
     * Met à jour le mot de passe d'un professeur.
     *
     * @param matricule   L'identifiant du professeur.
     * @param newPassword Le nouveau mot de passe.
     * @return ResponseEntity contenant un message indiquant le succès ou l'échec de la mise à jour.
     */
    public ResponseEntity<String> updateProfPassword(Long matricule, String newPassword) {
        Optional<Professeur> professeur = professeurRepository.findById(matricule);
        if (professeur.isPresent()) {
            if (newPassword.length() < 15) {
                return new ResponseEntity<>("The password must be at least 15 characters long", HttpStatus.BAD_REQUEST);
            }
            professeur.get().setPassword(newPassword);
            professeurRepository.save(professeur.get());
            return new ResponseEntity<>("Success update of password", HttpStatus.OK);
        }
        return new ResponseEntity<>("Teacher doesn't exist", HttpStatus.NOT_FOUND);
    }

    /**
     * Récupère les cours d'un professeur.
     *
     * @param email    L'email du professeur.
     * @param password Le mot de passe du professeur.
     * @return ResponseEntity contenant la liste des cours du professeur.
     */
    public ResponseEntity<List<String>> getCourses(String  email,String password){
        Long matricule = Long.valueOf(authService.extractMatriculeFromEmail(email));
        Optional<Professeur> professeurOptional = professeurRepository.findById(matricule);
        if (professeurOptional.isPresent()){
            Professeur professeur = professeurOptional.get();
            if (professeur.getPassword().equals(password)){
                List<String> result = professeur.getCourseList();
                return new ResponseEntity<>(result , HttpStatus.OK);
            }
            return new ResponseEntity<>(null , HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Associe un cours à un professeur.
     *
     * @param coursName Le nom du cours.
     * @param matricule L'identifiant du professeur.
     * @return ResponseEntity contenant un message indiquant le succès ou l'échec de l'association.
     */
    public ResponseEntity<String> attributeCourse(String coursName , Long matricule){
        Optional<Professeur> professeurOptional = professeurRepository.findById(matricule);//chercher le prof avec son matricule
        if (professeurOptional.isPresent()){
            Professeur professeur = professeurOptional.get();//recuperer le professeur
            ResponseEntity<Cours> coursSearch = coursService.getCoursByName(coursName);//recuperer le cours en utilisant son nom
            if(coursSearch.getStatusCode().is2xxSuccessful()){
                Cours cours = coursSearch.getBody(); //recuperer le cours
                List<String> courseList = professeur.getCourseList(); //recuperer liste de cours de ce prof
                List<String> teacherList = Objects.requireNonNull(cours).getListOfAllteachersToThisCours();//liste de profs de ce cours
                if (!(courseList.contains(coursName))){
                    courseList.add(coursName);
                    professeur.setCourseList(courseList);
                    professeurRepository.save(professeur);
                }else{
                    return new ResponseEntity<>("teacher have already this course" , HttpStatus.BAD_REQUEST);
                }
                if (!(teacherList.contains(professeur.getName()))){ //verifier si la liste de profs de ce cours contienne deja ce prof
                    teacherList.add(professeur.getName());
                    cours.setListOfAllteachersToThisCours(teacherList);
                    coursRepository.save(cours);
                }
                return new ResponseEntity<>("attribute success" , HttpStatus.OK);
            }
            return new ResponseEntity<>("course not found" , HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("teacher not found" , HttpStatus.NOT_FOUND);
    }

    /**
     * Génère un mot de passe aléatoire.
     *
     * @return Le mot de passe généré.
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

    /**
     * Supprime un cours de la liste des cours des professeurs.
     *
     * @param coursName Le nom du cours à supprimer.
     * @return ResponseEntity contenant un message indiquant le succès ou l'échec de la suppression.
     */
    public ResponseEntity<String> deleteCoursFromProfesseur(String coursName) {
        // Supprimer le cours de la liste de cours des professeurs
        List<String> affectedProfessors = professeurRepository.deleteCourseFromProfessors(coursName);

        // Supprimer chaque professeur de la liste de professeurs associés à ce cours
        coursRepository.deleteProfesseurFromCourse(coursName, affectedProfessors);

        return new ResponseEntity<>("delete success", HttpStatus.OK);
    }

    public Professeur findProfesseurByName(String teacherName) {
        String queryString = "SELECT p FROM Professeur p WHERE p.name = :teacherName";
        try {
            return (Professeur) entityManager.createQuery(queryString)
                    .setParameter("teacherName", teacherName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
