package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.extensionOussama.model.ReservationReason;
import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.model.RoomReservation;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomReservationRepository;
import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.model.User;
import com.genieLogiciel.Umons.repository.StudentRepository;
import com.genieLogiciel.Umons.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing room reservations.
 */
@Service
public class RoomReservationService {

    @Autowired
    private RoomReservationRepository roomReservationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a room reservation request.
     * Verifies the existence of the requester, the reservation date, and room availability.
     *
     * @param reservationRequest The room reservation request to add.
     * @return A ResponseEntity containing a message indicating the result of the operation.
     *         If successful, the status code is OK (200).
     *         If the user is not found, the status code is NOT_FOUND (404).
     *         If the reservation date is in the past or the reservation time is invalid,
     *         or if the room is not found, the status code is BAD_REQUEST (400).
     */
    public ResponseEntity<String> addRoomReservationRequest(RoomReservation reservationRequest) {
        String applicantName = reservationRequest.getFirstName() + " " + reservationRequest.getLastName();

        // Check if the applicant exists by name
        Optional<User> userOptional = userRepository.findFirstByName(applicantName);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"User not found\"}");
        }

        // Check the reservation date
        LocalDate currentDate = LocalDate.now();
        try {
            LocalDate localReservationDate = reservationRequest.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (localReservationDate.isBefore(currentDate)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Reservation date cannot be in the past\"}");
            }

            // Check the reservation time
            int startHour = Integer.parseInt(reservationRequest.getStart().split(":")[0]);
            if (startHour < 8 || startHour >= 23) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Reservation time must be between 8 AM and 11 PM\"}");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Invalid reservation date format\"}");
        }

        // Check if the desired room exists
        Optional<Room> roomOptional = roomRepository.getRoomByName(reservationRequest.getDesiredRoom());
        if (roomOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Room not found\"}");
        }

        reservationRequest.setReservationStatus(RoomReservation.ReservationStatus.PENDING);
        roomReservationRepository.save(reservationRequest);
        return ResponseEntity.ok("{\"message\":\"Reservation request submitted successfully\"}");
    }

    /**
     * Retrieves all possible reservation reasons.
     *
     * @return A list of all reservation reasons.
     */
    public List<ReservationReason> getAllReservationReasons() {
        return List.of(ReservationReason.values());
    }

    /**
     * Cancels a reservation by its ID.
     *
     * @param reservationId The ID of the reservation to cancel.
     * @return A ResponseEntity containing a message indicating the result of the operation.
     *         If successful, the status code is OK (200).
     *         If the reservation is not found, the status code is NOT_FOUND (404).
     */
    public ResponseEntity<String> cancelReservation(Long reservationId) {
        Optional<RoomReservation> roomReservation = roomReservationRepository.findById(reservationId);
        if (roomReservation.isEmpty()) {
            return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
        }
        roomReservationRepository.deleteById(reservationId);
        return new ResponseEntity<>("Delete success", HttpStatus.OK);
    }

    /**
     * Retrieves all reservation requests with pagination.
     *
     * @param page  The page number to retrieve.
     * @param limit The number of items per page.
     * @return A list of all reservation requests for the specified page.
     */
    public List<RoomReservation> getAllReservationRequests(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<RoomReservation> reservationsPage = roomReservationRepository.findAll(pageable);
        return reservationsPage.getContent();
    }

    /**
     * Retrieves the status of a reservation by its ID.
     *
     * @param reservationId The ID of the reservation.
     * @return A ResponseEntity containing the reservation status.
     *         If the reservation is not found, the status code is NOT_FOUND (404).
     */
    public ResponseEntity<RoomReservation.ReservationStatus> getStatus(Long reservationId) {
        Optional<RoomReservation> reservation = roomReservationRepository.findById(reservationId);
        if (reservation.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        RoomReservation roomReservation = reservation.get();
        return new ResponseEntity<>(roomReservation.getReservationStatus(), HttpStatus.OK);
    }

    /**
     * Finds a reservation by the student's ID (matricule).
     *
     * @param matricule The ID of the student.
     * @return A ResponseEntity containing the reservation associated with the student, if found.
     *         If the student or reservation is not found, the status code is NOT_FOUND (404).
     */
    public ResponseEntity<RoomReservation> findReservationByApplicantId(Long matricule) {
        Optional<Student> studentOptional = studentRepository.findById(matricule);
        if (studentOptional.isPresent()) {
            // Implement logic to retrieve reservation by student ID
            // This is a placeholder and should be replaced with actual implementation
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Verifies and updates expired reservations.
     * Sets the status of reservations that are still pending but have a past date to EXPIRED.
     */
    public void verifyAndUpdateExpiredReservations() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Convert LocalDate to Date
        Date currentDateAsDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Retrieve pending reservations with a past date
        List<RoomReservation> expiredReservations = roomReservationRepository
                .findByReservationStatusAndDateBefore(RoomReservation.ReservationStatus.PENDING, currentDateAsDate);

        // Update the status of expired reservations
        for (RoomReservation reservation : expiredReservations) {
            reservation.setReservationStatus(RoomReservation.ReservationStatus.EXPIRED);
            roomReservationRepository.save(reservation);
        }
    }

    /**
     * Executes the verification and update of expired reservations at regular intervals.
     * Runs every second.
     */
    @Scheduled(fixedRate = 1000) // Executes every second
    public void executeVerifyAndUpdateExpiredReservations() {
        verifyAndUpdateExpiredReservations();
    }

    /**
     * Updates the status of a reservation by its ID.
     *
     * @param reservationId The ID of the reservation.
     * @param newStatus     The new status to apply.
     * @return A ResponseEntity containing a message indicating the result of the operation.
     *         If successful, the status code is OK (200).
     *         If the reservation is not found, the status code is NOT_FOUND (404).
     */
    public ResponseEntity<String> updateRoomReservationStatus(Long reservationId, RoomReservation.ReservationStatus newStatus) {
        Optional<RoomReservation> reservationOptional = roomReservationRepository.findById(reservationId);
        if (reservationOptional.isEmpty()) {
            return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
        }
        RoomReservation reservation = reservationOptional.get();
        reservation.setReservationStatus(newStatus);
        roomReservationRepository.save(reservation);
        return new ResponseEntity<>("Status updated successfully", HttpStatus.OK);
    }
}
