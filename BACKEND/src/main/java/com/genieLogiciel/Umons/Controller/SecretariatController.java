package com.genieLogiciel.Umons.Controller;

import com.genieLogiciel.Umons.model.Secretariat;
import com.genieLogiciel.Umons.model.SignUpRequest;
import com.genieLogiciel.Umons.repository.SignUpRequestRepository;
import com.genieLogiciel.Umons.service.SecretariatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "secretariat")
public class SecretariatController {
    @Autowired
    private SignUpRequestRepository signUpRequestRepository;
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

    @GetMapping("/requests")
    public ResponseEntity<List<SignUpRequest>> getAllSignUpRequests() {
        List<SignUpRequest> signUpRequests = signUpRequestRepository.findAll();
        return ResponseEntity.ok(signUpRequests);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveSignup(@PathVariable Long id) {
        SignUpRequest request = signUpRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("accepted");
        signUpRequestRepository.save(request);
        return ResponseEntity.ok("Signup request approved!");
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectSignup(@PathVariable Long id) {
        SignUpRequest request = signUpRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus("rejected");
        signUpRequestRepository.save(request);
        return ResponseEntity.ok("Signup request rejected!");
    }
}