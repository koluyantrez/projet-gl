package com.genieLogiciel.Umons.backend.extensionOussama.model;

import com.genieLogiciel.Umons.backend.model.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Administrateur {


    private String name;

    @Enumerated(EnumType.STRING)
    private Category category = Category.ADMIN;

    private String password;
    private String email;
    @Id
    @GeneratedValue
    private Long id;


}
