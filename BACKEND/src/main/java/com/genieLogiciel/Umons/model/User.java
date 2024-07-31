package com.genieLogiciel.Umons.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cette classe représente un utilisateur de l'université.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public abstract class User {
    /**
     * Identifiant unique de l'utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matricule;

    /**
     * Prénom de l'utilisateur.
     */
    private String firstName;

    /**
     * Nom de famille de l'utilisateur.
     */
    private String lastName;

    /**
     * Nom complet de l'utilisateur.
     */
    private String name;

    /**
     * Adresse e-mail de l'utilisateur.
     */
    private String email;

    /**
     * Adresse de l'utilisateur.
     */
    private String adresse;

    /**
     * Numéro de téléphone de l'utilisateur.
     */
    private Long numero;

    /**
     * Mot de passe du compte de l'utilisateur.
     */
    private String password;

    /**
     * Image de l'utilisateur
     */

    private byte[] image;
}
