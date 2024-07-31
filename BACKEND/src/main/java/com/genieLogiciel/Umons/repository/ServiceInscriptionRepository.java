package com.genieLogiciel.Umons.repository;

import com.genieLogiciel.Umons.model.ServiceInscription;
import org.springframework.data.jpa.repository.JpaRepository;
;

public interface ServiceInscriptionRepository extends JpaRepository<ServiceInscription, Integer> {

    ServiceInscription findByMatricule(int matricule);
}