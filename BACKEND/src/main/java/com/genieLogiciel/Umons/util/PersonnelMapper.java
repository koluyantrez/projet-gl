package com.genieLogiciel.Umons.util;

import com.genieLogiciel.Umons.extensionOussama.model.Administrateur;
import com.genieLogiciel.Umons.extensionOussama.model.Assistant;
import com.genieLogiciel.Umons.model.*;
import com.genieLogiciel.Umons.repository.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonnelMapper {

    @Autowired private PersonnelRepository personnelRepository;

    public void mapToPersonnel(User user) {
        System.out.println("Reçoit le user pour maper to personnel : " + user.getName());
        Personnel personnel = new Personnel();
        personnel.setMatricule(user.getMatricule());
        personnel.setEmail(user.getEmail());
        personnel.setNumero(user.getNumero());
        personnel.setName(user.getName());
        personnel.setAdresse(user.getAdresse());
        if (user instanceof Professeur){
            personnel.setCategorie(Category.PROFESSEUR);
            personnel.setDepartement(((Professeur)user).getDepartement());
        }else if (user instanceof Assistant){
            personnel.setCategorie(Category.ASSISTANT);
            personnel.setDepartement(((Assistant)user).getDepartement());
        }else if (user instanceof Administrateur){
            personnel.setCategorie(Category.ADMIN);
        }else if (user instanceof ServiceInscription){
            personnel.setCategorie(Category.MembreSI);
        }
        personnelRepository.save(personnel);
    }

}
