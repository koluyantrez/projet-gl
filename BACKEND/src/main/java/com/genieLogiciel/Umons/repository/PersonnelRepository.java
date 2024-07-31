package com.genieLogiciel.Umons.repository;

import com.genieLogiciel.Umons.model.Category;
import com.genieLogiciel.Umons.model.Departement;
import com.genieLogiciel.Umons.model.Personnel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonnelRepository extends JpaRepository<Personnel,Long> {
    List<Personnel> findByCategorie(Category categorie);

    Optional<Personnel> findByName(String name);

    // Méthode pour trouver les personnels par département
    List<Personnel> findByDepartement(Departement departement);

    // Trouver par département et catégorie
    List<Personnel> findByDepartementAndCategorie(Departement departement, Category categorie);

    @Transactional
    void deleteByCategorie(Category categorie);


}
