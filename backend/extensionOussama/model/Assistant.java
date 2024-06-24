package com.genieLogiciel.Umons.backend.extensionOussama.model;
import com.genieLogiciel.Umons.backend.model.Category;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Random;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Assistant {
    @Id
    @GeneratedValue
    private Long matricule;

    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String adresse;
    private Long numero;
    private String departement;
    private Category category = Category.ASSISTANT;

    @ElementCollection
    private List<String> filieres;

    @ElementCollection
    private List<String> courseList;

    private String password;

    @PrePersist
    private void generateMatricule() {
        if (matricule == null) {
            // Générer un matricule aléatoire avec 6 chiffres
            Random random = new Random();
            int minMatriculeValue = 1_000_000;
            int maxMatriculeValue = 9_999_999;
            int generatedMatricule;
            do {
                generatedMatricule = random.nextInt(maxMatriculeValue - minMatriculeValue + 1) + minMatriculeValue;
            } while (String.valueOf(generatedMatricule).length() < 6);
            this.matricule = (long) generatedMatricule;
        }
    }
}