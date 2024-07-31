package com.genieLogiciel.Umons.extensionOussama.repository;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {

    @Transactional
    default void deleteProfesseurFromCourse(String courseName, List<String> professeurNames) {
        // Récupérer le cours par son nom
        Optional<Cours> coursOptional = findByName(courseName);
        if (coursOptional.isPresent())
        {
         Cours cours = coursOptional.get();
            // Supprimer chaque professeur de la liste de professeurs du cours
            for (String professeurName : professeurNames) {
                cours.getListOfAllteachersToThisCours().remove(professeurName);
            }
            save(cours); // Enregistrer les modifications dans la base de données
        }

    }

    Optional<Cours> findByName(String name);

    boolean existsByName(String name);

}