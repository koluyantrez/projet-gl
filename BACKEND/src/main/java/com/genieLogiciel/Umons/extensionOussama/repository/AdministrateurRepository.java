package com.genieLogiciel.Umons.extensionOussama.repository;

import com.genieLogiciel.Umons.extensionOussama.model.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur,Long> {
}
