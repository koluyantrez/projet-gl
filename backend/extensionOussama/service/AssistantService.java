package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.auth.service.AbstractLoginService;
import com.genieLogiciel.Umons.auth.service.AuthService;
import com.genieLogiciel.Umons.auth.service.EmailDomain;
import com.genieLogiciel.Umons.extensionOussama.model.Assistant;
import com.genieLogiciel.Umons.model.Category;
import com.genieLogiciel.Umons.model.Personnel;
import com.genieLogiciel.Umons.repository.PersonnelRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.AssistantRepository;
import com.genieLogiciel.Umons.service.UserService;
import com.genieLogiciel.Umons.util.PersonnelMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des assistants.
 */
@Service
public class AssistantService extends AbstractLoginService {

    private final AssistantRepository assistantRepository;
    private final PersonnelRepository personnelRepository;
    private final UserService userService;
    private final PersonnelMapper personnelMapper;

    @Autowired
    public AssistantService(AssistantRepository assistantRepository,
                            EntityManager entityManager,
                            PersonnelRepository personnelRepository,
                            AuthService authService,
                            UserService userService, PersonnelMapper personnelMapper) {
        super(authService);
        this.assistantRepository = assistantRepository;
        this.personnelRepository = personnelRepository;
        this.personnelMapper = personnelMapper;
        this.authService = authService;
        this.userService = userService;
    }

    public ResponseEntity<String> addNewAssistant(Assistant newAssistant) {
        ResponseEntity<String> response = userService.addUser(newAssistant, EmailDomain.ASSISTANT);
        if (response.getStatusCode() == HttpStatus.OK) {
            assistantRepository.save(newAssistant);
            personnelMapper.mapToPersonnel(newAssistant);
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return new ResponseEntity<>("Assistant already exists", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    public ResponseEntity<Assistant> getAssistantByName(String assistantName) {
        Optional<Assistant> assistantOptional = assistantRepository.findByName(assistantName);

        return assistantOptional.map(assistant -> new ResponseEntity<>(assistant, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Assistant getAssistantById(Long matricule){
        Optional<Assistant> assistantOptional =  assistantRepository.findById(matricule);
        return assistantOptional.orElse(null);
    }

    public List<Assistant> getAllAssistants(){
        return assistantRepository.findAll();
    }

    public ResponseEntity<String> deleteAssistantById(Long matricule) {
        Optional<Assistant> assistantOptional = assistantRepository.findById(matricule);
        if (assistantOptional.isPresent()) {
            Assistant assistant = assistantOptional.get();
            // Vérifier si l'assistant existe dans la liste des personnels
            Optional<Personnel> personnelOptional = personnelRepository.findById(matricule);
            personnelOptional.ifPresent(personnelRepository::delete); // Supprimer l'assistant de la liste des personnels
            assistantRepository.deleteById(matricule); // Supprimer l'assistant de la base de données des assistants
            return new ResponseEntity<>("Delete success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Assistant not found", HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<String> authenticate(Long matricule, String password) {
        Optional<Assistant> assistantOptional = assistantRepository.findById(matricule);
        if (assistantOptional.isPresent()) {
            Assistant assistant = assistantOptional.get();
            if (assistant.getPassword().equals(password)) {
                return ResponseEntity.ok().build(); // Login successful
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Invalid password
            }
        }
        return ResponseEntity.notFound().build(); // Assistant not found
    }

    /*private Personnel mapToPersonnel(Assistant assistant) {
        Personnel newPersonnel = new Personnel();
        newPersonnel.setDepartement(assistant.getDepartement());
        newPersonnel.setMatricule(assistant.getMatricule());
        newPersonnel.setName(assistant.getName());
        newPersonnel.setAdresse(assistant.getAdresse());
        newPersonnel.setNumero(assistant.getNumero());
        newPersonnel.setFilieres(new ArrayList<>(Optional.ofNullable(assistant.getFilieres()).orElse(new ArrayList<>())));
        newPersonnel.setEmail(assistant.getEmail());
        newPersonnel.setCategorie(Category.ASSISTANT);
        return newPersonnel;
    }*/
}
