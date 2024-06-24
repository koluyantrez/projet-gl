package com.genieLogiciel.Umons.backend.Controller;

import com.genieLogiciel.Umons.backend.model.Secretariat;
import com.genieLogiciel.Umons.backend.service.SecretariatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "secretariat")
public class SecretariatController {
    private SecretariatService secretariatService;

    public SecretariatController(SecretariatService secretariatService) {
        this.secretariatService = secretariatService;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping( consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> creer(@RequestBody Secretariat secretariat){
        this.secretariatService.creer(secretariat);
        return new ResponseEntity<>("Secretary member added successfully", HttpStatus.OK);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Secretariat> rechercher(){
        return this.secretariatService.rechercher();
    }

    @GetMapping(path="{matricule}",produces = APPLICATION_JSON_VALUE)
    public Secretariat lire(@PathVariable int matricule){
        return this.secretariatService.lire(matricule);
    }


    @DeleteMapping(path="{matricule}",produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> supprimer(@PathVariable int matricule){
        this.secretariatService.supprimer(matricule);
        return new ResponseEntity<>("Secretary member deleted successfully", HttpStatus.OK);
    }
}
