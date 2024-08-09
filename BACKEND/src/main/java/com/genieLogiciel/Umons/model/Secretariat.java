package com.genieLogiciel.Umons.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cette classe représente un membre du secrétariat de l'université.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Secretariat extends User {


    /**
     * Indique si le membre du secrétariat est directeur.
     */
    private Integer directeur;

    /**
     * Filière du secrétariat.
     */
    private String filiere;

    /**
     * Zone du secrétariat.
     */
    private String zone;

    private boolean isJuryMember;
}
