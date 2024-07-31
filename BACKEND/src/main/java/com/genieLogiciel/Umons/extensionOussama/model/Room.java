package com.genieLogiciel.Umons.extensionOussama.model;

import jakarta.persistence.*;
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
    @GeneratedValue
    private Long id;

    private String name;
    private String type;
    private int capacity;
    private String associatedBuilding;
    private Boolean available;
    private RoomStatus status;

    @ElementCollection
    private List<String> equipement;

}
