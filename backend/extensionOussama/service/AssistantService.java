package com.genieLogiciel.Umons.backend.extensionOussama.service;

import com.genieLogiciel.Umons.backend.auth.service.AuthService;
import com.genieLogiciel.Umons.backend.extensionOussama.model.Assistant;
import com.genieLogiciel.Umons.backend.model.Category;
import com.genieLogiciel.Umons.backend.model.Personnel;
import com.genieLogiciel.Umons.backend.repository.PersonnelRepository;
import com.genieLogiciel.Umons.backend.extensionOussama.repository.AssistantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service pour la gestion des assistants.
 */
@Service
public class AssistantService {

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private AuthService authService;

    /**
     * Ajoute un nouvel assistant.
     *
     * @param newAssistant Nouvel assistant à ajouter
     * @return ResponseEntity avec un message indiquant le succès ou l'échec de l'opération
     */
    public ResponseEntity<String> addNewAssistant(Assistant newAssistant) {
        List<Assistant> assistants = assistantRepository.findAll();
        for (Assistant assistant : assistants) {
            if (assistant.getFirstName().equals(newAssistant.getFirstName()) && assistant.getLastName().equals(newAssistant.getLastName())) {
                return new ResponseEntity<>("Assistant already exists.", HttpStatus.BAD_REQUEST);
            }
        }

        newAssistant.setName(newAssistant.getFirstName() + " " + newAssistant.getLastName());
        newAssistant.setPassword(generatePassword());
        assistantRepository.save(newAssistant);

        newAssistant.setEmail(newAssistant.getMatricule() + "@Illumis.assistant.ac.be");
        assistantRepository.save(newAssistant);

        Personnel newPersonnel = new Personnel();
        newPersonnel.setDepartement(newAssistant.getDepartement());
        newPersonnel.setMatricule(newAssistant.getMatricule());
        newPersonnel.setName(newAssistant.getName());
        newPersonnel.setAdresse(newAssistant.getAdresse());
        newPersonnel.setNumero(newAssistant.getNumero());
        List<String> listFilieres = new ArrayList<>(); // Initialisation de la liste
        if (newAssistant.getFilieres() != null) { // Vérification de nullité
            listFilieres.addAll(newAssistant.getFilieres());
        }
        newPersonnel.setFilieres(listFilieres);
        newPersonnel.setEmail(newAssistant.getEmail());
        newPersonnel.setCategorie(Category.ASSISTANT);
        personnelRepository.save(newPersonnel);

        return new ResponseEntity<>("Assistant added successfully.", HttpStatus.OK);
    }

    /**
     * Récupère un assistant par son nom.
     *
     * @param assistantName Nom de l'assistant à récupérer
     * @return L'assistant trouvé, null sinon
     */
    public Assistant getAssistantByName(String assistantName) {
        String queryString = "SELECT a FROM Assistant a WHERE a.name = :assistantName";
        try {
            return (Assistant) entityManager.createQuery(queryString)
                    .setParameter("assistantName", assistantName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Récupère un assistant par son identifiant.
     *
     * @param matricule Identifiant de l'assistant à récupérer
     * @return L'assistant trouvé, null sinon
     */
    public Assistant getAssistantById(Long matricule){
        Optional<Assistant> assistantOptional =  assistantRepository.findById(matricule);
        if (assistantOptional.isPresent()){
            Assistant assistant = assistantOptional.get();
            new ResponseEntity<>("assistant exist ", HttpStatus.OK);
            return assistant;
        }
        new ResponseEntity<>("assistant not found" , HttpStatus.NOT_FOUND);
        return null;
    }

    /**
     * Récupère tous les assistants.
     *
     * @return Liste de tous les assistants
     */
    public List<Assistant> getAllAssistants(){
        List<Assistant> listAssistants = assistantRepository.findAll();
        new ResponseEntity<>("get list of assistant" , HttpStatus.OK);
        return listAssistants;
    }

    /**
     * Supprime un assistant par son identifiant.
     *
     * @param matricule Identifiant de l'assistant à supprimer
     * @return ResponseEntity avec un message indiquant le succès ou l'échec de l'opération
     */
    public ResponseEntity<String> deleteAssistantById(Long matricule) {
        Optional<Assistant> assistantOptional = assistantRepository.findById(matricule);
        if (assistantOptional.isPresent()) {
            Assistant assistant = assistantOptional.get();
            // Vérifier si l'assistant existe dans la liste des personnels
            Optional<Personnel> personnelOptional = personnelRepository.findById(matricule);
            if (personnelOptional.isPresent()) {
                Personnel personnel = personnelOptional.get();
                personnelRepository.delete(personnel); // Supprimer l'assistant de la liste des personnels
            }
            assistantRepository.deleteById(matricule); // Supprimer l'assistant de la base de données des assistants
            return new ResponseEntity<>("delete success", HttpStatus.OK);
        }
        return new ResponseEntity<>("assistant not found", HttpStatus.BAD_REQUEST);
    }

    /**
     * Génère un mot de passe aléatoire pour un assistant.
     *
     * @return Mot de passe généré
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
     * Gère l'authentification d'un assistant.
     *
     * @param email    Email de l'assistant
     * @param password Mot de passe de l'assistant
     * @return ResponseEntity avec un message indiquant le succès ou l'échec de l'opération
     */
    public ResponseEntity<String> login(String email, String password) {
        String matriculeStr = authService.extractMatriculeFromEmail(email);
        System.out.println("Dans la methode login assistant : " );
        System.out.println("email = "+ email);
        System.out.println("password = " + password);
        System.out.println("Matricule str =  "+ matriculeStr);
        if (matriculeStr == null) {
            return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
        }

        try {
            Long matricule = Long.parseLong(matriculeStr);
            Optional<Assistant> assistantOptional = assistantRepository.findById(matricule);
            if (assistantOptional.isPresent()) {
                Assistant assistant = assistantOptional.get();
                if (assistant.getPassword().equals(password)) {
                    return new ResponseEntity<>("Login successful", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>("Assistant not found", HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid matricule format", HttpStatus.BAD_REQUEST);
        }
    }
}
