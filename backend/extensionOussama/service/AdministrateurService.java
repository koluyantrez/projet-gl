package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.auth.service.AbstractLoginService;
import com.genieLogiciel.Umons.auth.service.AuthService;
import com.genieLogiciel.Umons.auth.service.EmailDomain;
import com.genieLogiciel.Umons.extensionOussama.model.Administrateur;
import com.genieLogiciel.Umons.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.extensionOussama.repository.AdministrateurRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.BatimentRepository;
import com.genieLogiciel.Umons.model.Category;
import com.genieLogiciel.Umons.model.Personnel;
import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.repository.PersonnelRepository;
import com.genieLogiciel.Umons.service.ProfesseurService;
import com.genieLogiciel.Umons.service.UserService;
import com.genieLogiciel.Umons.util.PersonnelMapper;
import com.google.zxing.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrateurService extends AbstractLoginService {

    @Autowired private AdministrateurRepository administrateurRepository;
    @Autowired private PersonnelRepository personnelRepository;
    private final UserService userService;
    private final PersonnelMapper personnelMapper;

    public AdministrateurService(AuthService authService, UserService userService, PersonnelMapper personnelMapper) {
        super(authService);
        this.userService = userService;
        this.personnelMapper = personnelMapper;
    }

    public ResponseEntity<String> addAdmin(Administrateur newAdmin){
        ResponseEntity<String> response = userService.addUser(newAdmin, EmailDomain.ADMIN);
        if (response.getStatusCode() == HttpStatus.OK) {
            administrateurRepository.save(newAdmin);
            personnelMapper.mapToPersonnel(newAdmin);
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return new ResponseEntity<>("Admin already exists", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    public ResponseEntity<Administrateur> getAdminById(Long ID){
        Optional<Administrateur> administrateurOptional = administrateurRepository.findById(ID);
        if (administrateurOptional.isPresent()){
            Administrateur administrateur = administrateurOptional.get();
            return new ResponseEntity<>(administrateur , HttpStatus.OK);
        }
        return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
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

    /**
     * @param matricule
     * @param password
     * @return
     */
    @Override
    protected ResponseEntity<String> authenticate(Long matricule, String password) {
        Optional<Administrateur> administrateurOptional = administrateurRepository.findById(matricule);
        if (administrateurOptional.isPresent()) {
            Administrateur administrateur = administrateurOptional.get();
            if (administrateur.getPassword().equals(password)) {
                return ResponseEntity.ok().build(); // Login successful
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Invalid password
            }
        }
        return ResponseEntity.notFound().build(); // Admin not found
    }


}
