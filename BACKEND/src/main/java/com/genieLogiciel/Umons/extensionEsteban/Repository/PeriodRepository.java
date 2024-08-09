package com.genieLogiciel.Umons.extensionEsteban.Repository;

import com.genieLogiciel.Umons.extensionEsteban.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Period repository.
 */
public interface PeriodRepository extends JpaRepository<Period, Long> {
    /**
     * Find top by order by id desc period.
     *
     * @return the period
     */
    Period findTopByOrderByIdDesc();

//    @Query("SELECT p FROM Period p WHERE p.isCurrent = true")
//    Period findCurrentPeriod();
}