package com.genieLogiciel.Umons.backend.repository;

import com.genieLogiciel.Umons.backend.model.Secretariat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecretariatRepository extends JpaRepository<Secretariat, Integer> {
    Secretariat findByMatricule(int matricule);
}