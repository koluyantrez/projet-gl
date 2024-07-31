package com.genieLogiciel.Umons.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class AbstractLoginService {

    protected AuthService authService;

    public AbstractLoginService(AuthService authService) {
        this.authService = authService;
    }

    public ResponseEntity<String> login(String email, String password) {
        String matriculeStr = authService.extractMatriculeFromEmail(email);
        if (matriculeStr == null) {
            return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
        }

        try {
            Long matricule = Long.parseLong(matriculeStr);
            ResponseEntity<String> result = authenticate(matricule, password);
            if (result != null) {
                return result;
            }
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid matricule format", HttpStatus.BAD_REQUEST);
        }
    }

    protected abstract ResponseEntity<String> authenticate(Long matricule, String password);
}
