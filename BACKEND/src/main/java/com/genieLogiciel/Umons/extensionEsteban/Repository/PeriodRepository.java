package com.genieLogiciel.Umons.extensionEsteban.Repository;

import com.genieLogiciel.Umons.extensionEsteban.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodRepository extends JpaRepository<Period, Long> {
    Period findTopByOrderByIdDesc();
}