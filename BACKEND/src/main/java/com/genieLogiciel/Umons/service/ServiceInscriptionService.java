package com.genieLogiciel.Umons.service;

import com.genieLogiciel.Umons.auth.service.AbstractLoginService;
import com.genieLogiciel.Umons.auth.service.AuthService;
import com.genieLogiciel.Umons.auth.service.EmailDomain;
import com.genieLogiciel.Umons.model.*;
import com.genieLogiciel.Umons.repository.PersonnelRepository;
import com.genieLogiciel.Umons.repository.ServiceInscriptionRepository;
import com.genieLogiciel.Umons.util.PersonnelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les membres du Service Inscription.
 */
@Service
@CrossOrigin(origins = "http://localhost:3000")
public class ServiceInscriptionService extends AbstractLoginService {
    private ServiceInscriptionRepository serviceInscriptionRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AuthService authService;

    @Autowired private final UserService userService;

    private final PersonnelMapper personnelMapper;

    /**
     * Construit un nouveau ServiceInscriptionService.
     *
     * @param serviceInscriptionRepository Le repository pour les membres du Service Inscription.
     */
    public ServiceInscriptionService(ServiceInscriptionRepository serviceInscriptionRepository, AuthService authService, UserService userService, PersonnelMapper personnelMapper) {
        super(authService);
        this.serviceInscriptionRepository = serviceInscriptionRepository;
        this.userService = userService;
        this.personnelMapper = personnelMapper;
    }


    /**
     * Crée un nouveau membre du Service Inscription.
     *
     * @param newServiceInscription Le nouveau membre du Service Inscription à créer.
     * @return ResponseEntity indiquant le succès ou l'échec de l'opération.
     */
    public ResponseEntity<String> addNewMember(ServiceInscription newServiceInscription) {
        ResponseEntity<String> response = userService.addUser(newServiceInscription , EmailDomain.SERVICE_INSCRIPTION);
        if (response.getStatusCode() == HttpStatus.OK){
            serviceInscriptionRepository.save(newServiceInscription);
            personnelMapper.mapToPersonnel(newServiceInscription);
        }else if (response.getStatusCode() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>("Member already exist" , HttpStatus.OK);
        }
        return response;
    }

    /**
     * Recherche tous les membres du Service Inscription.
     *
     * @return Liste de tous les membres du Service Inscription.
     */
    public List<ServiceInscription> rechercher() {
        return this.serviceInscriptionRepository.findAll();
    }

    /**
     * Récupère un membre du Service Inscription par matricule.
     *
     * @param matricule Le matricule du membre du Service Inscription à récupérer.
     * @return Le membre du Service Inscription s'il est trouvé.
     * @throws RuntimeException si le membre du Service Inscription n'est pas trouvé.
     */
    public ServiceInscription lire(Integer matricule) {
        Optional<ServiceInscription> OptionalInsc = serviceInscriptionRepository.findById(matricule);
        if (OptionalInsc.isPresent()) {
            return OptionalInsc.get();
        }
        throw new RuntimeException("Membre du Service Inscription non trouvé");
    }

    /**
     * Supprime un membre du Service Inscription par matricule.
     *
     * @param matricule Le matricule du membre du Service Inscription à supprimer.
     * @return ResponseEntity indiquant le succès ou l'échec de l'opération.
     */
    public ResponseEntity<String> supprimer(int matricule) {
        this.serviceInscriptionRepository.deleteById(matricule);
        return new ResponseEntity<>("Membre du Service Inscription supprimé avec succès", HttpStatus.OK);
    }

    /**
     * Ajoute un nouvel étudiant.
     *
     * @param student L'étudiant à ajouter.
     * @return ResponseEntity indiquant le succès ou l'échec de l'opération.
     */
    public ResponseEntity<String> addStudent(Student student) {
        return studentService.addStudent(student);
    }

    /**
     * Authentifie un membre du Service Inscription.
     *
     * @param email    L'email du membre du Service Inscription.
     * @param password Le mot de passe du membre du Service Inscription.
     * @return ResponseEntity indiquant le succès ou l'échec de l'opération.
     */
    public ResponseEntity<String> login(String email, String password) {
        String matriculeStr = authService.extractMatriculeFromEmail(email);
        if (matriculeStr == null) {
            return new ResponseEntity<>("Format d'email invalide", HttpStatus.BAD_REQUEST);
        }

        try {
            Integer matricule = Integer.parseInt(matriculeStr);
            Optional<ServiceInscription> serviceInscriptionOptional = serviceInscriptionRepository.findById(matricule);
            if (serviceInscriptionOptional.isPresent()) {
                ServiceInscription membre_IS = serviceInscriptionOptional.get();
                if (membre_IS.getPassword().equals(password)) {
                    return new ResponseEntity<>("Connexion réussie", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Mot de passe invalide", HttpStatus.UNAUTHORIZED);
                }
            }
            return new ResponseEntity<>("Membre Service Inscription non trouvé", HttpStatus.NOT_FOUND);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Format de matricule invalide", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param matricule
     * @param password
     * @return
     */
    @Override
    protected ResponseEntity<String> authenticate(Long matricule, String password) {
        return null;
    }
}
