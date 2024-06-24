package com.genieLogiciel.Umons.backend.extensionOussama.repository;

import com.genieLogiciel.Umons.backend.extensionOussama.model.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface RoomReservationRepository extends JpaRepository<RoomReservation,Long> {

    @Query("SELECT r FROM RoomReservation r WHERE r.reservationStatus = ?1 AND r.date < ?2")
    List<RoomReservation> findByReservationStatusAndDateBefore(RoomReservation.ReservationStatus reservationStatus, Date date);

}
