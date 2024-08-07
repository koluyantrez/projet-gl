package com.genieLogiciel.Umons.backend.Controller;

import com.genieLogiciel.Umons.backend.model.SignUpRequest;
import com.genieLogiciel.Umons.backend.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guest")
public class SignUpController {

    @Autowired
    private SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signUpRequest) {
        signUpService.sendSignupRequest(signUpRequest);
        return ResponseEntity.ok("Signup request sent");
    }

    @DeleteMapping("/signup/{id}")
    public ResponseEntity<String> deleteSignupRequest(@PathVariable Long id) {
        signUpService.deleteSignupRequest(id);
        return ResponseEntity.ok("Signup request deleted");
    }
}
