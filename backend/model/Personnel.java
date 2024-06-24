package com.genieLogiciel.Umons.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Cette classe représente un membre du personnel de l'université.
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Personnel {
    /**
     * Identifiant unique du membre du personnel.
     */
    @Id
    @Pattern(regexp = "\\d{6}", message = "Le matricule doit être un nombre à 6 chiffres")
    private Long matricule;

    /**
     * Nom complet du membre du personnel.
     */
    private String name;

    /**
     * Adresse e-mail du membre du personnel.
     */
    private String email;

    /**
     * Numéro de téléphone du membre du personnel.
     */
    private Long numero;

    /**
     * Adresse du membre du personnel.
     */
    private String adresse;

    /**
     * Département auquel le membre du personnel est associé.
     */
    private String departement;

    /**
     * Catégorie à laquelle le membre du personnel appartient.
     */
    @Enumerated(EnumType.STRING)
    private Category categorie;

    /**
     * Liste des filières associées au membre du personnel (uniquement pour les professeurs).
     */
    @ElementCollection
    private List<String> filieres;
}
