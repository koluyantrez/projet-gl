package com.genieLogiciel.Umons.backend.extensionOussama.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {
    @Id
    private Long id;

    private String name;
    private String type;
    private int capacity;
    private String associatedBuilding;

    @ElementCollection
    private List<String> equipement;

}
