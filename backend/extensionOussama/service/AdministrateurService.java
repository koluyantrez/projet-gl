package com.genieLogiciel.Umons.backend.extensionOussama.service;

import com.genieLogiciel.Umons.backend.auth.service.AuthService;
import com.genieLogiciel.Umons.backend.extensionOussama.model.Administrateur;
import com.genieLogiciel.Umons.backend.extensionOussama.repository.AdministrateurRepository;
import com.genieLogiciel.Umons.backend.model.Category;
import com.genieLogiciel.Umons.backend.model.Personnel;
import com.genieLogiciel.Umons.backend.repository.PersonnelRepository;
import com.genieLogiciel.Umons.backend.service.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrateurService {

    @Autowired private AdministrateurRepository administrateurRepository;
    @Autowired private ProfesseurService professeurService;
    @Autowired private PersonnelRepository personnelRepository;
    @Autowired private AuthService authService;

    public ResponseEntity<String> addAdmin(Administrateur newAdmin){
        List<Administrateur> administrateurs = administrateurRepository.findAll();
        for (Administrateur administrateur : administrateurs) {
            if (administrateur.getName().equals(newAdmin.getName())) {
                return new ResponseEntity<>("admin already exists.", HttpStatus.BAD_REQUEST);
            }
        }
        newAdmin.setPassword(professeurService.generatePassword());
        administrateurRepository.save(newAdmin);
        newAdmin.setEmail(newAdmin.getId() + "@Illumis.admin.ac.be");
        administrateurRepository.save(newAdmin);

        Personnel newPersonnel = new Personnel();
        newPersonnel.setMatricule(newAdmin.getId());
        newPersonnel.setName(newAdmin.getName());

        newPersonnel.setEmail(newAdmin.getEmail());
        newPersonnel.setCategorie(Category.ADMIN);
        personnelRepository.save(newPersonnel);

        return new ResponseEntity<>("admin added successfully.", HttpStatus.OK);
    }

    public ResponseEntity<Administrateur> getAdminById(Long ID){
        Optional<Administrateur> administrateurOptional = administrateurRepository.findById(ID);
        if (administrateurOptional.isPresent()){
            Administrateur administrateur = administrateurOptional.get();
            return new ResponseEntity<>(administrateur , HttpStatus.OK);
        }
        return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> login(String email, String password) {
        String matriculeStr = authService.extractMatriculeFromEmail(email);
        System.out.println("Dans la methode login admin : " );
        System.out.println("email = "+ email);
        System.out.println("password = " + password);
        System.out.println("Matricule str =  "+ matriculeStr);
        if (matriculeStr == null) {
            return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
        }

        try {
            Long matricule = Long.parseLong(matriculeStr);
            Optional<Administrateur> administrateurOptional = administrateurRepository.findById(matricule);
            if (administrateurOptional.isPresent()) {
                Administrateur administrateur = administrateurOptional.get();
                if (administrateur.getPassword().equals(password)) {
                    return new ResponseEntity<>("Login successful", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Invalid password", HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>("admin not found", HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid id format", HttpStatus.BAD_REQUEST);
        }
    }

    public List<Administrateur> getAllAdmin(){
        return administrateurRepository.findAll();
    }

    public ResponseEntity<String> deleteAdminById(Long matricule) {
        Optional<Administrateur> administrateurOptional = administrateurRepository.findById(matricule);
        if (administrateurOptional.isPresent()) {
            Administrateur administrateur = administrateurOptional.get();
            // Vérifier si l'admin existe dans la liste des personnels
            Optional<Personnel> personnelOptional = personnelRepository.findById(matricule);
            if (personnelOptional.isPresent()) {
                Personnel personnel = personnelOptional.get();
                personnelRepository.delete(personnel); // Supprimer l'admin de la liste des personnels
            }
            administrateurRepository.deleteById(matricule); // Supprimer l'admin de la base de données des admins
            return new ResponseEntity<>("delete success", HttpStatus.OK);
        }
        return new ResponseEntity<>("admin not found", HttpStatus.BAD_REQUEST);
    }

}
