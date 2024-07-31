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
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String associatedPersonnel;
    private String location; // Peut inclure des informations sur le bâtiment et l'étage
    private Boolean available;
    private boolean deletionPending=false;

    @ElementCollection
    private List<String> equipment;


    private String batiment; // Suppose que le bureau est associé à un bâtiment

}
