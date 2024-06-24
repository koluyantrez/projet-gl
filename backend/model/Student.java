package com.genieLogiciel.Umons.backend.model;

import com.genieLogiciel.Umons.backend.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.backend.extensionOussama.model.Cours;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Cette classe représente un étudiant de l'université.
 */
@Entity
@NoArgsConstructor
@Setter
@Getter
public class Student {
    /**
     * Identifiant unique de l'étudiant.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Utilisation de la stratégie IDENTITY pour générer le matricule automatiquement
    @Pattern(regexp = "\\d{8}", message = "Le matricule doit être un nombre à 8 chiffres")
    private Long matricule;

    /**
     * Prénom de l'étudiant.
     */
    private String firstName;

    /**
     * Nom de famille de l'étudiant.
     */
    private String lastName;

    /**
     * Adresse e-mail de l'étudiant.
     */
    private String email;

    /**
     * Nom complet de l'étudiant.
     */
    private String name;

    /**
     * Numéro de téléphone de l'étudiant.
     */
    private Long numero;

    /**
     * Filière de l'étudiant.
     */
    private String filiere;

    /**
     * Département de l'étudiant.
     */
    private String departement;

    /**
     * Mot de passe du compte de l'étudiant.
     */
    private String password;

    /**
     * Année d'études de l'étudiant.
     */
    private String annee;

    /**
     * Indicateur d'inscription de l'étudiant.
     */
    private Boolean inscrit;

    /**
     * Adresse de l'étudiant.
     */
    private String adresse;

    /**
     * Liste des cours réussis par l'étudiant.
     */
    @ElementCollection
    private List<String> coursReussie; //liste de cours deja validés

    /**
     * Liste des anciens PAE de l'étudiant.
     */
    @OneToMany
    private List<Pae> oldPAE; //liste des old pae

    /**
     * PAE actuel de l'étudiant.
     */
    @OneToOne
    private Pae actuelPAE; //pae actuel

    /**
     * Liste des cours actuels de l'étudiant.
     */
    @ManyToMany
    private List<Cours> listOfActuelCours; // liste de cours actuel de l'etudiant
}
