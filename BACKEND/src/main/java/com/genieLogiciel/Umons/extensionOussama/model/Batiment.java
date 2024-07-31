package com.genieLogiciel.Umons.extensionOussama.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private Boolean available;
    private int numberOfRooms;
    private int numbersOfOffices;

    @ElementCollection
    private List<String> allRooms;

    @ElementCollection
    private List<String> allOffices;

    //liste des salles
}
