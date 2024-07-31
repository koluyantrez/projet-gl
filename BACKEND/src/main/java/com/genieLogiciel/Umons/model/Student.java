package com.genieLogiciel.Umons.model;

import com.genieLogiciel.Umons.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Cette classe représente un étudiant de l'université.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student extends User {
    /**
     * Filière de l'étudiant.
     */
    private String filiere;

    /**
     * Département de l'étudiant.
     */
    @Enumerated(EnumType.STRING)
    private Departement departement;

    /**
     * Année d'études de l'étudiant.
     */
    private String annee;

    /**
     * Indicateur d'inscription de l'étudiant.
     */
    private Boolean inscrit;

    /**
     * Liste des cours réussis par l'étudiant.
     */
    @ElementCollection
    private List<String> coursReussie;

    /**
     * Liste des anciens PAE de l'étudiant.
     */
    @OneToMany
    private List<Pae> oldPAE;

    /**
     * PAE actuel de l'étudiant.
     */
    @OneToOne
    private Pae actuelPAE;

    /**
     * Liste des cours actuels de l'étudiant.
     */
    @ManyToMany
    private List<Cours> listOfActuelCours;

    /**
     * L'image de l'étudiant.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;
}
