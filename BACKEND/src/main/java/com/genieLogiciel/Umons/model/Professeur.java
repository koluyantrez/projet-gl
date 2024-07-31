package com.genieLogiciel.Umons.model;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Cette classe représente un professeur de l'université.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Professeur extends User{
    /**
     * Matricule du membre du personnel de secrétariat auquel le professeur est associé.
     */
    private Integer secretariatMatricule;

    /**
     * Département auquel le professeur est associé.
     */
    @Enumerated(EnumType.STRING)
    private Departement departement;

    /**
     * Catégorie du personnel, dans ce cas, PROFESSEUR.
     */
    private Category category = Category.PROFESSEUR;

    /**
     * Liste des filières associées au professeur.
     */
    @ElementCollection
    private List<String> filieres;

    /**
     * Liste des cours enseignés par le professeur.
     */
    @ElementCollection
    private List<String> courseList;

    /**
     * Indique si le professeur est titulaire.
     */
    private Integer titulaire;
}
