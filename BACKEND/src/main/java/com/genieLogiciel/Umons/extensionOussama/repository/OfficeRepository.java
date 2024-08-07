package com.genieLogiciel.Umons.extensionOussama.repository;

import com.genieLogiciel.Umons.extensionOussama.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    Optional<Office> findByName(String name);
    List<Office> findByBatiment(String building);

    void deleteByName(String name);

}
