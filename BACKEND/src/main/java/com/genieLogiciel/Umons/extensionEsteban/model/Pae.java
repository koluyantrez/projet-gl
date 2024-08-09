package com.genieLogiciel.Umons.extensionEsteban.model;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The type Pae.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pae {
    @Id
    @GeneratedValue
    private Long matricule;
    private String annee;
    private Integer totalCredits;
    private String commentaire;


    @ManyToOne
    private Student student;

    @ManyToMany
    private List<Cours> courses;
}
