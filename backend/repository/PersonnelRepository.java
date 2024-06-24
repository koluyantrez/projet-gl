package com.genieLogiciel.Umons.backend.repository;

import com.genieLogiciel.Umons.backend.model.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonnelRepository extends JpaRepository<Personnel,Long> {
}
