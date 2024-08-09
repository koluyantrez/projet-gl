package com.genieLogiciel.Umons.extensionEsteban.Controller;

import com.genieLogiciel.Umons.extensionEsteban.Service.JuryMemberService;
import com.genieLogiciel.Umons.model.Professeur;
import com.genieLogiciel.Umons.model.Secretariat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jury-members")
public class JuryMemberController {

    @Autowired
    private JuryMemberService juryMemberService;

    @GetMapping("/professeurs")
    public ResponseEntity<List<Professeur>> getJuryProfesseurs() {
        List<Professeur> juryProfesseurs = juryMemberService.getJuryProfesseurs();
        return ResponseEntity.ok(juryProfesseurs);
    }

    @GetMapping("/secretaires")
    public ResponseEntity<List<Secretariat>> getJurySecretaires() {
        List<Secretariat> jurySecretaires = juryMemberService.getJurySecretaires();
        return ResponseEntity.ok(jurySecretaires);
    }
}