package com.genieLogiciel.Umons.backend.extensionOussama.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Batiment {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String adresse;
    private Boolean disponible;

    //liste des salles
}
