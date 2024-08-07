package com.genieLogiciel.Umons.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Cette classe représente un membre du service d'inscription de l'université.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
public class ServiceInscription extends User {
    /**
     * Matricule du membre du personnel de secrétariat auquel le membre du service d'inscription est associé.
     */
    private Long secretariatMatricule;
}
