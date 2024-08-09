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

//    public boolean isWithinPeriod() {
//        Period period = periodRepository.findCurrentPeriod();
//        if (period == null) {
//            return false;
//        }
//        LocalDate now = LocalDate.now();
//        return now.isAfter(period.getStartDate()) && now.isBefore(period.getEndDate());
//    }

//    public Period setCurrentPeriod(LocalDate startDate, LocalDate endDate) {
//        Period period = periodRepository.findCurrentPeriod();
//        if (period == null) {
//            period = new Period();
//        }
//        period.setStartDate(startDate);
//        period.setEndDate(endDate);
//        period.setCurrent(true);
//        return periodRepository.save(period);
//    }
}