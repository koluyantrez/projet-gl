package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.RoomReservation;
import com.genieLogiciel.Umons.extensionOussama.model.ReservationReason;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomReservationRepository;
import com.genieLogiciel.Umons.extensionOussama.service.RoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling room reservation requests.
 */
@RestController
@RequestMapping("/api")
public class RoomReservationController {

    @Autowired
    private RoomReservationService roomReservationService;
    @Autowired
    private RoomReservationRepository roomReservationRepository;

    /**
     * Adds a new room reservation request.
     *
     * @param reservationRequest The room reservation request to be added.
     * @return ResponseEntity containing a success message.
     */
    @PostMapping("/roomReservation/addReservation")
    public ResponseEntity<String> addRoomReservationRequest(@RequestBody RoomReservation reservationRequest) {
        return roomReservationService.addRoomReservationRequest(reservationRequest);
    }

    /**
     * Cancels a room reservation.
     *
     * @param id The ID of the reservation to be canceled.
     * @return ResponseEntity containing a success message.
     */
    @DeleteMapping("/roomReservation/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable Long id) {
        return roomReservationService.cancelReservation(id);
    }

    /**
     * Retrieves a room reservation by its ID.
     *
     * @param id The ID of the reservation to be retrieved.
     * @return ResponseEntity containing the room reservation.
     */
    @GetMapping("/reservation/{id}")
    public ResponseEntity<RoomReservation> getReservationById(@PathVariable Long id) {
        Optional<RoomReservation> reservation = roomReservationRepository.findById(id);
        return reservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all room reservation requests.
     *
     * @param page The page number to retrieve.
     * @param limit The maximum number of results per page.
     * @return ResponseEntity containing the list of room reservation requests.
     */
    @GetMapping("/reservations")
    public ResponseEntity<List<RoomReservation>> getAllReservationRequest(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        List<RoomReservation> reservations = roomReservationService.getAllReservationRequests(page, limit);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    /**
     * Retrieves the status of a room reservation.
     *
     * @param id The ID of the reservation.
     * @return ResponseEntity containing the reservation status.
     */
    @GetMapping("/reservation/status/{id}")
    public ResponseEntity<RoomReservation.ReservationStatus> getStatus(@PathVariable Long id) {
        return roomReservationService.getStatus(id);
    }

    /**
     * Finds a room reservation by the applicant's ID.
     *
     * @param matricule The ID of the applicant.
     * @return ResponseEntity containing the room reservation.
     */
    @GetMapping("/reservation/applicant/{matricule}")
    public ResponseEntity<RoomReservation> findReservationByApplicantId(@PathVariable Long matricule) {
        return roomReservationService.findReservationByApplicantId(matricule);
    }

    /**
     * Retrieves all the available reservation reasons.
     *
     * @return ResponseEntity containing the list of reservation reasons.
     */
    @GetMapping("/reservation/reasons")
    public ResponseEntity<List<ReservationReason>> getAllReservationReasons() {
        List<ReservationReason> reasons = roomReservationService.getAllReservationReasons();
        return new ResponseEntity<>(reasons, HttpStatus.OK);
    }

    /**
     * Updates the status of a room reservation.
     *
     * @param reservationId The ID of the reservation to be updated.
     * @param newStatus The new status to be applied.
     * @return ResponseEntity containing a success message.
     */
    @PutMapping("/{reservationId}/status")
    public ResponseEntity<String> updateRoomReservationStatus(
            @PathVariable Long reservationId,
            @RequestParam RoomReservation.ReservationStatus newStatus) {
        return roomReservationService.updateRoomReservationStatus(reservationId, newStatus);
    }

    /**
     * Verifies and updates expired room reservations.
     * This method is scheduled to run every second.
     */
    @Scheduled(fixedRate = 1000)
    public void executeVerifyAndUpdateExpiredReservations() {
        roomReservationService.verifyAndUpdateExpiredReservations();
    }
}