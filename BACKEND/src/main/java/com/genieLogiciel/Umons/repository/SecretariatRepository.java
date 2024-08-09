package com.genieLogiciel.Umons.repository;

import com.genieLogiciel.Umons.model.Secretariat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SecretariatRepository extends JpaRepository<Secretariat, Integer> {
    Secretariat findByMatricule(int matricule);

    List<Secretariat> findByIsJuryMemberTrue();
}