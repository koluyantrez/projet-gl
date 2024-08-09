package com.genieLogiciel.Umons.extensionEsteban.Service;

import com.genieLogiciel.Umons.extensionEsteban.Repository.PeriodRepository;
import com.genieLogiciel.Umons.extensionEsteban.model.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PeriodService {

    @Autowired
    private PeriodRepository periodRepository;

    public void savePeriod(Period period) {
        periodRepository.save(period);
    }

    public Period getCurrentPeriod() {
        return periodRepository.findTopByOrderByIdDesc();
    }

    public boolean isWithinPeriod() {
        Period period = getCurrentPeriod();
        LocalDate now = LocalDate.now();
        return now.isAfter(period.getStartDate()) && now.isBefore(period.getEndDate());
    }
}