package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.RoomReservation;
import com.genieLogiciel.Umons.extensionOussama.service.RoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des réservations de salles.
 */
@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/roomReservation")
public class RoomReservationController {

    @Autowired
    private RoomReservationService roomReservationService;

    /**
     * Ajoute une demande de réservation de salle.
     *
     * @param roomReservation La demande de réservation de salle à ajouter.
     * @return ResponseEntity contenant le message de succès ou d'erreur.
     */
    @PostMapping("/addReservation")
    public ResponseEntity<String> addRoomReservationRequest(@RequestBody RoomReservation roomReservation){
        return roomReservationService.addRoomReservationRequest(roomReservation);
    }
}
