package com.genieLogiciel.Umons.service;

import com.genieLogiciel.Umons.auth.service.AbstractLoginService;
import com.genieLogiciel.Umons.auth.service.AuthService;
import com.genieLogiciel.Umons.model.Secretariat;
import com.genieLogiciel.Umons.repository.SecretariatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecretariatService extends AbstractLoginService {
    private SecretariatRepository secretariatRepository;
    protected AuthService authService;

    public SecretariatService(SecretariatRepository secretariatRepository,AuthService authService) {
        super(authService);
        this.secretariatRepository = secretariatRepository;
    }

    public ResponseEntity<String> creer(Secretariat secretariat){
        Secretariat secrBDD = this.secretariatRepository.findByMatricule(Math.toIntExact(secretariat.getMatricule()));
        if(secrBDD != null){
            return new ResponseEntity<>("Secretary member already exists.", HttpStatus.BAD_REQUEST);

        }
        this.secretariatRepository.save(secretariat);
        return new ResponseEntity<>("Secretary member added successfully", HttpStatus.OK);
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
        return null;
    }
}