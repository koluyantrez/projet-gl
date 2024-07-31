package com.genieLogiciel.Umons.extensionOussama.service;

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
 * Service pour la gestion des réservations de salles.
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
     * Ajoute une demande de réservation de salle.
     *
     * @param reservationRequest La demande de réservation.
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    public ResponseEntity<String> addRoomReservationRequest(RoomReservation reservationRequest) {
        String applicantName = reservationRequest.getFirstName()+" "+reservationRequest.getLastName();
        // Vérification de l'existence du demandeur par nom
        Optional<User> userOptional = userRepository.findByName(applicantName);
        System.out.println("applicant name for request : " + applicantName);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Vérification de la date de réservation
        LocalDate currentDate = LocalDate.now();
        try {
            LocalDate localReservationDate = reservationRequest.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (localReservationDate.isBefore(currentDate)) {
                return new ResponseEntity<>("Reservation date cannot be in the past", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid reservation date format", HttpStatus.BAD_REQUEST);
        }

        // Vérification de l'existence de la salle souhaitée
        Optional<Room> roomOptional = roomRepository.getRoomByName(reservationRequest.getDesiredRoom());
        if (roomOptional.isEmpty()) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }

        reservationRequest.setReservationStatus(RoomReservation.ReservationStatus.PENDING);
        roomReservationRepository.save(reservationRequest);
        return new ResponseEntity<>("Reservation request submitted successfully", HttpStatus.OK);
    }

    /**
     * Annule une réservation par son ID.
     *
     * @param reservationId L'ID de la réservation à annuler.
     * @return ResponseEntity contenant un message sur le succès de l'opération.
     */
    public ResponseEntity<String> cancelReservation(Long reservationId){
        Optional<RoomReservation> roomReservation = roomReservationRepository.findById(reservationId);
        if (roomReservation.isEmpty()){
            return new ResponseEntity<>("Reservation not found", HttpStatus.NOT_FOUND);
        }
        roomReservationRepository.deleteById(reservationId);
        return new ResponseEntity<>("Delete success", HttpStatus.OK);
    }

    /**
     * Récupère toutes les demandes de réservation.
     *
     * @param page Numéro de la page à récupérer.
     * @param limit Nombre d'éléments par page.
     * @return ResponseEntity contenant la liste de toutes les réservations.
     */
    public ResponseEntity<List<RoomReservation>> getAllReservationRequest(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<RoomReservation> allReservations = roomReservationRepository.findAll(pageable);
        return new ResponseEntity<>(allReservations.getContent(), HttpStatus.OK);
    }

    /**
     * Récupère le statut d'une réservation par son ID.
     *
     * @param reservationId L'ID de la réservation.
     * @return ResponseEntity contenant le statut de la réservation.
     */
    public ResponseEntity<RoomReservation.ReservationStatus> getStatus(Long reservationId){
        Optional<RoomReservation> reservation = roomReservationRepository.findById(reservationId);
        if (reservation.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        RoomReservation roomReservation = reservation.get();
        return new ResponseEntity<>(roomReservation.getReservationStatus(), HttpStatus.OK);
    }

    /**
     * Récupère une réservation par l'ID de l'étudiant.
     *
     * @param matricule Le matricule de l'étudiant.
     * @return ResponseEntity contenant la réservation associée à l'étudiant, s'il existe.
     */
    public ResponseEntity<RoomReservation> findReservationByApplicantId(Long matricule){
        Optional<Student> studentOptional = studentRepository.findById(matricule);
        if (studentOptional.isPresent()){
            // Implémenter la logique pour récupérer la réservation par ID de l'étudiant
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Vérifie et met à jour les réservations expirées.
     */
    public void verifyAndUpdateExpiredReservations() {
        // Récupérer la date actuelle
        LocalDate currentDate = LocalDate.now();

        // Convertir LocalDate en Date
        Date currentDateAsDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Récupérer les réservations en attente avec une date dépassée
        List<RoomReservation> expiredReservations = roomReservationRepository
                .findByReservationStatusAndDateBefore(RoomReservation.ReservationStatus.PENDING, currentDateAsDate);

        // Mettre à jour les statuts des réservations expirées
        for (RoomReservation reservation : expiredReservations) {
            reservation.setReservationStatus(RoomReservation.ReservationStatus.EXPIRED);
            roomReservationRepository.save(reservation);
        }
    }

    /**
     * Exécute la vérification et la mise à jour des réservations expirées à intervalles réguliers.
     */
    @Scheduled(fixedRate = 1000) // Exécute toutes les secondes
    public void executeVerifyAndUpdateExpiredReservations() {
        verifyAndUpdateExpiredReservations();
    }
}
