package com.genieLogiciel.Umons.backend.repository;

import com.genieLogiciel.Umons.backend.model.ServiceInscription;
import org.springframework.data.jpa.repository.JpaRepository;
;

public interface ServiceInscriptionRepository extends JpaRepository<ServiceInscription, Integer> {

    ServiceInscription findByMatricule(int matricule);
}