package com.genieLogiciel.Umons.repository;

import com.genieLogiciel.Umons.model.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {

    @Transactional
    default List<String> deleteCourseFromProfessors(String courseName) {
        // Récupérer la liste de tous les professeurs ayant le cours spécifié dans leur liste de cours
        List<Professeur> professors = findByCourseListContaining(courseName);
        List<String> affectedProfessors = new ArrayList<>();

        // Pour chaque professeur trouvé, supprimer le cours de sa liste de cours
        professors.forEach(professeur -> {
            professeur.getCourseList().remove(courseName);
            save(professeur); // Enregistrer les modifications dans la base de données
            affectedProfessors.add(professeur.getName()); // Ajouter le nom du professeur affecté
        });

        return affectedProfessors;
    }

    List<Professeur> findByCourseListContaining(String courseName);

    Optional<Professeur> findByName(String name);

    List<Professeur> findByIsJuryMemberTrue();

}

