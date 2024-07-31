package com.genieLogiciel.Umons.extensionOussama.repository;

import com.genieLogiciel.Umons.extensionOussama.model.RoomReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {
    Page<RoomReservation> findAll(Pageable pageable);
    List<RoomReservation> findByReservationStatusAndDateBefore(RoomReservation.ReservationStatus status, Date date);
}
