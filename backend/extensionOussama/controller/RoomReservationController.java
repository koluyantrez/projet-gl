package com.genieLogiciel.Umons.backend.extensionOussama.controller;

import com.genieLogiciel.Umons.backend.extensionOussama.model.RoomReservation;
import com.genieLogiciel.Umons.backend.extensionOussama.service.RoomReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des réservations de salles.
 */
@RestController
@CrossOrigin("http://localhost:8080")
public class RoomReservationController {

    @Autowired
    private RoomReservationService roomReservationService;

    /**
     * Ajoute une demande de réservation de salle.
     *
     * @param roomReservation La demande de réservation de salle à ajouter.
     * @return ResponseEntity contenant le message de succès ou d'erreur.
     */
    @PostMapping("/addRoomReservationRequest")
    public ResponseEntity<String> addRoomReservationRequest(@RequestBody RoomReservation roomReservation){
        return roomReservationService.addRoomReservationRequest(roomReservation);
    }
}
