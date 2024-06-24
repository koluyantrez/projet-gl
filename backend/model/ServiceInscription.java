package com.genieLogiciel.Umons.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

/**
 * Cette classe représente un membre du service d'inscription de l'université.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class ServiceInscription {

    /**
     * Identifiant unique du membre du service d'inscription.
     */
    @Id
    @GeneratedValue
    @Pattern(regexp = "\\d{6}", message = "Le matricule doit être un nombre à 6 chiffres")
    private Long matricule;

    /**
     * Matricule du membre du personnel de secrétariat auquel le membre du service d'inscription est associé.
     */
    private Long secretariatMatricule;

    /**
     * Prénom du membre du service d'inscription.
     */
    private String firstName;

    /**
     * Nom de famille du membre du service d'inscription.
     */
    private String LastName;

    /**
     * Nom complet du membre du service d'inscription.
     */
    private String name;

    /**
     * Adresse du membre du service d'inscription.
     */
    private String Adresse;

    /**
     * Numéro de téléphone du membre du service d'inscription.
     */
    private Long numero;

    /**
     * Adresse e-mail du membre du service d'inscription.
     */
    private String email;

    /**
     * Mot de passe du compte du membre du service d'inscription.
     */
    private String password;
}
