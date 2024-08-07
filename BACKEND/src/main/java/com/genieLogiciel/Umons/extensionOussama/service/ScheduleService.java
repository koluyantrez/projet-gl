package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.model.Schedule;
import com.genieLogiciel.Umons.extensionOussama.repository.CoursRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    private final CoursRepository coursRepository;

    private final RoomRepository roomRepository;

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(CoursRepository coursRepository, RoomRepository roomRepository, ScheduleRepository scheduleRepository) {
        this.coursRepository = coursRepository;
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public ResponseEntity<String> createSchedule() {
        List<Cours> coursList = coursRepository.findAll();
        List<Room> roomList = roomRepository.findAll();

        for (Cours cours : coursList) {
            boolean assigned = false;

            for (String timeSlot : getTimeSlots()) {
                if (assigned) break;

                for (Room room : roomList) {
                    if (room.getCapacity() >= cours.getStudentList().size() && isRoomAvailable(room, timeSlot) && isTeacherAvailable(cours, timeSlot)) {
                        Schedule schedule = new Schedule();
                        schedule.setCours(cours);
                        schedule.setRoom(room);
                        schedule.setTimeSlot(timeSlot);

                        scheduleRepository.save(schedule);

                        assigned = true;
                        break;
                    }
                }
            }

            if (!assigned) {
                return new ResponseEntity<>("Unable to assign room for course: " + cours.getName(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>("Schedule created successfully", HttpStatus.OK);
    }

    private boolean isRoomAvailable(Room room, String timeSlot) {
        List<Schedule> schedules = scheduleRepository.findByRoomAndTimeSlot(room, timeSlot);
        return schedules.isEmpty();
    }

    private boolean isTeacherAvailable(Cours cours, String timeSlot) {
        List<Schedule> schedules = scheduleRepository.findByTimeSlot(timeSlot);
        for (Schedule schedule : schedules) {
            if (schedule.getCours().getTeacherName().equals(cours.getTeacherName())) {
                return false;
            }
        }
        return true;
    }

    private List<String> getTimeSlots() {
        // Return a list of predefined time slots
        return List.of("Monday 9-11", "Monday 11-13", "Monday 14-16", "Tuesday 9-11", "Tuesday 11-13", "Tuesday 14-16", "Wednesday 9-11", "Wednesday 11-13", "Wednesday 14-16", "Thursday 9-11", "Thursday 11-13", "Thursday 14-16", "Friday 9-11", "Friday 11-13", "Friday 14-16");
    }
}
