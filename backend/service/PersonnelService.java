package com.genieLogiciel.Umons.backend.service;

import com.genieLogiciel.Umons.backend.extensionOussama.repository.AssistantRepository;
import com.genieLogiciel.Umons.backend.model.Category;
import com.genieLogiciel.Umons.backend.model.Personnel;
import com.genieLogiciel.Umons.backend.repository.PersonnelRepository;
import com.genieLogiciel.Umons.backend.repository.ProfesseurRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer les opérations liées au personnel de l'université.
 */
@Service
public class PersonnelService {

    @Autowired
    private PersonnelRepository personnelRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private AssistantRepository assistantRepository;

    /**
     * Récupère tous les membres du personnel.
     *
     * @return La liste de tous les membres du personnel.
     */
    public List<Personnel> getAllPersonnel(){
        List<Personnel> all = personnelRepository.findAll();
        return all;
    }

    /**
     * Récupère les membres du personnel par département.
     *
     * @param departement Le département des membres du personnel à rechercher.
     * @return La liste des membres du personnel dans le département spécifié.
     */
    public List<Personnel> getPersonnelByDepartement(String departement) {
        String sql = "SELECT * FROM personnel WHERE departement = :departement";
        Query query = entityManager.createNativeQuery(sql, Personnel.class);
        query.setParameter("departement", departement);
        List<Personnel> personnelList = query.getResultList();
        return personnelList;
    }

    /**
     * Récupère les membres du personnel par catégorie.
     *
     * @param category La catégorie des membres du personnel à rechercher.
     * @return La liste des membres du personnel dans la catégorie spécifiée.
     */
    public List<Personnel> getPersonnelByCategory(String category) {
        category = category.toUpperCase();
        String sql = "SELECT * FROM personnel WHERE categorie = :category";
        Query query = entityManager.createNativeQuery(sql, Personnel.class);
        query.setParameter("category", category);
        List<Personnel> personnelList = query.getResultList();
        return personnelList;
    }

    /**
     * Récupère un membre du personnel par identifiant.
     *
     * @param matricule L'identifiant du membre du personnel à récupérer.
     * @return Le membre du personnel correspondant à l'identifiant spécifié.
     */
    public Personnel getPersonnelById(Long matricule){
        Optional<Personnel> personnelOptional = personnelRepository.findById(matricule);
        if (personnelOptional.isPresent()){
            Personnel personnel = personnelOptional.get();
            new ResponseEntity<>("personnel exist" , HttpStatus.OK);
            return personnel;
        }
        new ResponseEntity<>("personnel not found" , HttpStatus.NOT_FOUND);
        return null;
    }

    /**
     * Récupère un membre du personnel par nom.
     *
     * @param personnelName Le nom du membre du personnel à récupérer.
     * @return Le membre du personnel correspondant au nom spécifié, ou null s'il n'est pas trouvé.
     */
    public Personnel getPersonnelByName(String personnelName) {
        String queryString = "SELECT p FROM Personnel p WHERE p.name = :personnelName";
        try {
            return (Personnel) entityManager.createQuery(queryString)
                    .setParameter("personnelName", personnelName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Supprime un membre du personnel par identifiant.
     *
     * @param matricule L'identifiant du membre du personnel à supprimer.
     * @return ResponseEntity contenant un message indiquant le succès ou l'échec de la suppression.
     */
    public ResponseEntity<String> deletePersonnelById(Long matricule){
        Optional<Personnel> personnelOptional = personnelRepository.findById(matricule);
        if (personnelOptional.isPresent()){
            Personnel personnel = personnelOptional.get();
            Category category = personnel.getCategorie();

            //supprimer d'abord ce personnel dans la liste associée
            switch (category){
                case PROFESSEUR :
                    professeurRepository.deleteById(matricule);
                    break;
                case ASSISTANT:
                    assistantRepository.deleteById(matricule);
                    break;
                case TECHNICIEN, SECRETARIAT:
                    //manque encore ces deux cas;
                    break;
            }
            personnelRepository.deleteById(matricule); //supprimer ce personnel dans la liste de personnel
            return new ResponseEntity<>("delete success" , HttpStatus.OK);

        }
        return new ResponseEntity<>("personnelNotFound" , HttpStatus.NOT_FOUND);
    }

    /**
     * Supprime un membre du personnel par nom.
     *
     * @param name Le nom du membre du personnel à supprimer.
     * @return ResponseEntity contenant un message indiquant le succès ou l'échec de la suppression.
     */
    public ResponseEntity<String> deletePersonnelByName(String name){
        Personnel personnel = getPersonnelByName(name);
        if (personnel == null){
            return new ResponseEntity<>("personnel not found" , HttpStatus.NOT_FOUND);
        }
        Long idPersonnel = personnel.getMatricule();
        Category categoryPersonnel = personnel.getCategorie();
        switch (categoryPersonnel){
            case PROFESSEUR :
                professeurRepository.deleteById(idPersonnel);
                break;
            case ASSISTANT:
                assistantRepository.deleteById(idPersonnel);
                break;
            case TECHNICIEN, SECRETARIAT:
                //manque encore ces deux cas;
                break;
        }
        personnelRepository.deleteById(idPersonnel);
        return new ResponseEntity<>("delete success" , HttpStatus.OK);
    }
}
