package com.genieLogiciel.Umons.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Secretariat {
    @Id
    @GeneratedValue
    @Pattern(regexp = "\\d{6}", message = "Matricule must be a 6-digit number")
    private Long matricule;
    private String Name;
    private String mail;
    private Integer directeur;
    private String filiere;
    private String zone;
    private String password;
}
