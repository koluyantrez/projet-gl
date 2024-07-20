package com.genieLogiciel.Umons.extensionOussama.model;
import com.genieLogiciel.Umons.model.Category;
import com.genieLogiciel.Umons.model.User;
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
public class Assistant extends User {

    private String departement;
    private Category category = Category.ASSISTANT;

    @ElementCollection
    private List<String> filieres;

    @ElementCollection
    private List<String> courseList;

}