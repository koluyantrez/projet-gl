package com.genieLogiciel.Umons.extensionOussama.repository;

import com.genieLogiciel.Umons.extensionOussama.model.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssistantRepository extends JpaRepository<Assistant , Long> {
    Optional<Assistant> findByName(String name);

}
