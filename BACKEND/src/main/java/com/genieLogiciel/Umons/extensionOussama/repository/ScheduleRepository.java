package com.genieLogiciel.Umons.extensionOussama.repository;

import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByRoomAndTimeSlot(Room room, String timeSlot);
    List<Schedule> findByTimeSlot(String timeSlot);
}
