package com.genieLogiciel.Umons.backend.extensionOussama.repository;

import com.genieLogiciel.Umons.backend.extensionOussama.model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {

    @Transactional
    default void deleteProfesseurFromCourse(String courseName, List<String> professeurNames) {
        // Récupérer le cours par son nom
        Cours cours = findByName(courseName);

        if (cours != null) {
            // Supprimer chaque professeur de la liste de professeurs du cours
            for (String professeurName : professeurNames) {
                cours.getListOfAllteachersToThisCours().remove(professeurName);
            }
            save(cours); // Enregistrer les modifications dans la base de données
        }
    }

    Cours findByName(String name);
}