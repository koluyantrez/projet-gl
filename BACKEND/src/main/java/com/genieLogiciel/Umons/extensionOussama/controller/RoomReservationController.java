package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.RoomReservation;
import com.genieLogiciel.Umons.extensionOussama.service.RoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoomReservationController {

    @Autowired
    private RoomReservationService roomReservationService;

    @PostMapping("/roomReservation/addReservation")
    public ResponseEntity<String> addRoomReservationRequest(@RequestBody RoomReservation reservationRequest) {
        return roomReservationService.addRoomReservationRequest(reservationRequest);
    }

    @DeleteMapping("/roomReservation/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        return roomReservationService.cancelReservation(id);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<RoomReservation>> getAllReservationRequest(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return roomReservationService.getAllReservationRequest(page, limit);
    }

    @GetMapping("/reservation/status/{id}")
    public ResponseEntity<RoomReservation.ReservationStatus> getStatus(@PathVariable Long id) {
        return roomReservationService.getStatus(id);
    }

    @GetMapping("/reservation/applicant/{matricule}")
    public ResponseEntity<RoomReservation> findReservationByApplicantId(@PathVariable Long matricule) {
        return roomReservationService.findReservationByApplicantId(matricule);
    }

    @Scheduled(fixedRate = 1000) // Exécute toutes les secondes
    public void executeVerifyAndUpdateExpiredReservations() {
        roomReservationService.verifyAndUpdateExpiredReservations();
    }
}
