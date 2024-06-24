package com.genieLogiciel.Umons.backend.extensionOussama.repository;

import com.genieLogiciel.Umons.backend.extensionOussama.model.Batiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatimentRepository extends JpaRepository<Batiment,Long> {
}
