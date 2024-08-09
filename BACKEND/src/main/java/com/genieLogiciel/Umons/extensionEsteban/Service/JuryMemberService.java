package com.genieLogiciel.Umons.extensionEsteban.Service;

import com.genieLogiciel.Umons.repository.ProfesseurRepository;
import com.genieLogiciel.Umons.repository.SecretariatRepository;
import com.genieLogiciel.Umons.model.Professeur;
import com.genieLogiciel.Umons.model.Secretariat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuryMemberService {

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private SecretariatRepository secretaireRepository;

    public List<Professeur> getJuryProfesseurs() {
        return professeurRepository.findByIsJuryMemberTrue();
    }

    public List<Secretariat> getJurySecretaires() {
        return secretaireRepository.findByIsJuryMemberTrue();
    }
}
