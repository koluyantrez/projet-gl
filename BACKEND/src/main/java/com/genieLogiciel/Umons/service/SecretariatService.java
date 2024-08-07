package com.genieLogiciel.Umons.service;

import com.genieLogiciel.Umons.auth.service.AbstractLoginService;
import com.genieLogiciel.Umons.auth.service.AuthService;
import com.genieLogiciel.Umons.auth.service.EmailDomain;
import com.genieLogiciel.Umons.model.Secretariat;
import com.genieLogiciel.Umons.repository.SecretariatRepository;
import com.genieLogiciel.Umons.util.PersonnelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecretariatService extends AbstractLoginService {
    private SecretariatRepository secretariatRepository;

    @Autowired
    private UserService userService;
    @Autowired private PersonnelMapper personnelMapper;

    public SecretariatService(SecretariatRepository secretariatRepository,AuthService authService) {
        super(authService);
        this.secretariatRepository = secretariatRepository;
    }

    public ResponseEntity<String> creer(Secretariat secretariat){
        ResponseEntity<String> response = userService.addUser(secretariat , EmailDomain.SECRETARIAT);
        if (response.getStatusCode() == HttpStatus.OK){
            secretariatRepository.save(secretariat);
            personnelMapper.mapToPersonnel(secretariat);
        }else if (response.getStatusCode() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>("Member already exist" , HttpStatus.OK);
        }
        return response;
    }

    public List<Secretariat> rechercher(){
        return this.secretariatRepository.findAll();
    }

    public Secretariat lire(int matricule) {
        Optional<Secretariat> optionalSecretariat = Optional.ofNullable(this.secretariatRepository.findByMatricule(matricule));
        if(optionalSecretariat.isPresent()){
            return optionalSecretariat.get();
        }
        throw new RuntimeException("Secretary member not found");
    }

    public ResponseEntity<String> supprimer(int matricule) {
        this.secretariatRepository.deleteById(matricule);
        return new ResponseEntity<>("Secretary deleted successfully", HttpStatus.OK);
    }

    /**
     * @param matricule
     * @param password
     * @return
     */
    @Override
    protected ResponseEntity<String> authenticate(Long matricule, String password) {
        return secretariatRepository.findById(Math.toIntExact(matricule))
                .map(secretariat -> {
                    if (secretariat.getPassword().equals(password)) {
                        return ResponseEntity.ok("Login successful");
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
                    }
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found"));
    }
}