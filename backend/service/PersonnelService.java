package com.genieLogiciel.Umons.service;

import com.genieLogiciel.Umons.extensionOussama.repository.AssistantRepository;
import com.genieLogiciel.Umons.model.Category;
import com.genieLogiciel.Umons.model.Personnel;
import com.genieLogiciel.Umons.repository.PersonnelRepository;
import com.genieLogiciel.Umons.repository.ProfesseurRepository;
import com.genieLogiciel.Umons.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonnelService {

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired private UserRepository userRepository;

    public List<Personnel> getAllPersonnel() {
        return personnelRepository.findAll();
    }

    public List<Personnel> getPersonnelByDepartement(String departement) {
        return personnelRepository.findByDepartement(departement);
    }

    public List<Personnel> getPersonnelByCategory(String category) {
        Category enumCategory = Category.valueOf(category.toUpperCase());
        return personnelRepository.findByCategorie(enumCategory);
    }

    public ResponseEntity<Personnel> getPersonnelById(Long matricule) {
        Optional<Personnel> personnelOptional = personnelRepository.findById(matricule);
        return personnelOptional
                .map(personnel -> ResponseEntity.ok(personnel))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    public ResponseEntity<Personnel> getPersonnelByName(String personnelName) {
        Optional<Personnel> optionalPersonnel = personnelRepository.findByName(personnelName);
        return optionalPersonnel
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    public ResponseEntity<String> deletePersonnelById(Long matricule) {
        Optional<Personnel> personnelOptional = personnelRepository.findById(matricule);
        if (personnelOptional.isPresent()){
            //userRepository.findById(matricule).ifPresent(userRepository :: delete);
            personnelRepository.findById(matricule).ifPresent(personnelRepository :: delete);
            return new ResponseEntity<>("Delete succes" , HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found" , HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> deletePersonnelByName(String name) {
        Optional<Personnel> personnelOptional = personnelRepository.findByName(name);
        if (personnelOptional.isPresent()) {
            Personnel personnel = personnelOptional.get();
            deletePersonnel(personnel);
            return new ResponseEntity<>("delete success", HttpStatus.OK);
        }
        return new ResponseEntity<>("personnel not found", HttpStatus.NOT_FOUND);
    }

    private void deletePersonnel(Personnel personnel) {
        Long idPersonnel = personnel.getMatricule();
        Category category = personnel.getCategorie();
        switch (category) {
            case PROFESSEUR:
                professeurRepository.deleteById(idPersonnel);
                break;
            case ASSISTANT:
                assistantRepository.deleteById(idPersonnel);
                break;
            case TECHNICIEN:
            case SECRETARIAT:
                // ajouter la logique de suppression pour ces catégories si nécessaire
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + category);
        }
        personnelRepository.deleteById(idPersonnel);
    }
}
