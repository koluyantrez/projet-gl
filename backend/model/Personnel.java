package com.genieLogiciel.Umons.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Cette classe représente un membre du personnel de l'université.
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Personnel{

    @Id
    private Long matricule;

    private String adresse;
    private String email;
    private String name;
    private Long numero;

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
