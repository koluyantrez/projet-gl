package com.genieLogiciel.Umons.auth.controller;

import com.genieLogiciel.Umons.auth.LoginRequest;
import com.genieLogiciel.Umons.auth.service.AbstractLoginService;
import com.genieLogiciel.Umons.service.ProfesseurService;
import com.genieLogiciel.Umons.service.ServiceInscriptionService;
import com.genieLogiciel.Umons.service.StudentService;
import com.genieLogiciel.Umons.extensionOussama.service.AssistantService;
import com.genieLogiciel.Umons.extensionOussama.service.AdministrateurService;
import com.genieLogiciel.Umons.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("http://localhost:8080")
public class AuthController {

    @Autowired
    private AuthService authService;

   @Autowired private AssistantService assistantService;
   @Autowired private StudentService studentService;
   @Autowired private ProfesseurService professeurService;
   @Autowired private AdministrateurService administrateurService;
   @Autowired private ServiceInscriptionService serviceInscriptionService;

    @PostMapping("/login_")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginData) {
        String email = loginData.getEmail();
        String password = loginData.getPassword();
        String role = authService.determineUserRole(email);

        ResponseEntity<String> response = handleLogin(role, email, password);

        return response != null ? response : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<String> handleLogin(String role, String email, String password) {
        return switch (role) {
            case "assistant" -> assistantService.login(email, password);
            case "professeur" -> professeurService.login(email, password);
            case "student" -> studentService.login(email, password);
            case "inscription" -> serviceInscriptionService.login(email, password);
            case "administrateur" -> administrateurService.login(email, password);
            default -> new ResponseEntity<>("Unknown role", HttpStatus.UNAUTHORIZED);
        };
    }
}