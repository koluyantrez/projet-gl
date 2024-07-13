package com.genieLogiciel.Umons.backend.model;

import com.genieLogiciel.Umons.backend.extensionOussama.model.Cours;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.sql.Blob;
import java.util.List;

/**
 * Cette classe représente un professeur de l'université.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Professeur {
    /**
     * Identifiant unique du professeur.
     */
    @Id
    @GeneratedValue
    @Pattern(regexp = "\\d{6}", message = "Le matricule doit être un nombre à 6 chiffres")
    private Long matricule;

    /**
     * Matricule du membre du personnel de secrétariat auquel le professeur est associé.
     */
    private Integer secretariatMatricule;

    /**
     * Prénom du professeur.
     */
    private String firstName;

    /**
     * Nom de famille du professeur.
     */
    private String LastName;

    /**
     * Nom complet du professeur.
     */
    private String name;

    /**
     * Adresse e-mail du professeur.
     */
    private String email;

    /**
     * Adresse du professeur.
     */
    private String adresse;

    /**
     * Numéro de téléphone du professeur.
     */
    private Long numero;

    /**
     * Département auquel le professeur est associé.
     */
    private String departement;

    /**
     * Catégorie du personnel, dans ce cas, PROFESSEUR.
     */
    private Category category = Category.PROFESSEUR;

    /**
     * Filière principale du professeur
     */
    private String filiere;

    /**
     * Liste des filières associées au professeur.
     */
    //@ElementCollection
    //private List<String> filieres;

    /**
     * Liste des cours enseignés par le professeur.
     */
    @ElementCollection
    private List<String> courseList;

    /**
     * Indique si le professeur est titulaire.
     */
    private Integer titulaire;

    /**
     * Mot de passe du compte du professeur.
     */
    private String password;

    /**
     * Image du professeur
     */
    private byte[] image;
}
