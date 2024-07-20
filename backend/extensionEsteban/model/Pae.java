package com.genieLogiciel.Umons.extensionEsteban.model;

import com.genieLogiciel.Umons.model.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pae {
    @Id
    private Long matricule;
    private String annee;
    private Integer totalCredits;
    private String commentaire;
    private Integer validation;

    @ManyToOne
    private Student student;
}
