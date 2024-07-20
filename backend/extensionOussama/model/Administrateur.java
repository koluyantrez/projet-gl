package com.genieLogiciel.Umons.extensionOussama.model;

import com.genieLogiciel.Umons.model.Category;
import com.genieLogiciel.Umons.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Administrateur extends User {



    @Enumerated(EnumType.STRING)
    private Category category = Category.ADMIN;

}
