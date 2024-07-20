package com.genieLogiciel.Umons.extensionOussama.model;

import com.genieLogiciel.Umons.model.Professeur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class  Cours {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String filiere;
    private String departement;
    private String teacherName;
    private String code;

    @ElementCollection
    private List<String> listOfAllteachersToThisCours;

    @ElementCollection
    private List<String> studentList;

}
