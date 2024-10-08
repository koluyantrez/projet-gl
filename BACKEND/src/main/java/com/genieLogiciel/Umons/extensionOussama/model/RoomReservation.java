package com.genieLogiciel.Umons.extensionOussama.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RoomReservation {

    @Id
    @GeneratedValue
    private Long id; // Identifiant unique de la réservation

    private String firstName;
    private String lastName;
    private Long applicantMatricule; // Identifiant de l'utilisateur effectuant la réservation
    private String desiredRoom; // Identifiant de la salle désirée pour la réservation
    private Date date;
    private String start;
    private String end;
    private Time time;
    private RoomStatus roomStatus;

    private ReservationStatus reservationStatus;

    @Enumerated(EnumType.STRING)
    private ReservationReason reason;


    public enum ReservationStatus {
        PENDING, // En attente
        APPROVED, // Approuvé
        REJECTED, // Rejeté
        CANCELED, // Annulé
        EXPIRED
    }


}
