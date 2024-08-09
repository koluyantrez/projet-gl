package com.genieLogiciel.Umons.extensionEsteban.model;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PAERequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    private String status = "pending";

    @ManyToMany
    private List<Cours> courses;

    // Getters and Setters
}