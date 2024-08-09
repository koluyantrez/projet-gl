package com.genieLogiciel.Umons.extensionEsteban.Service;


import com.genieLogiciel.Umons.extensionEsteban.Repository.PAERepository;
import com.genieLogiciel.Umons.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Pae service.
 */
@Service
public class PAEService {

    @Autowired
    private PAERepository paeRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Create pae pae.
     *
     * @param pae the pae
     * @return the pae
     */
    public Pae createPAE(Pae pae) {
        return paeRepository.save(pae);
    }

    /**
     * Gets all pa es.
     *
     * @return the all pa es
     */
    public List<Pae> getAllPAEs() {
        return paeRepository.findAll();
    }

    /**
     * Gets pae by id.
     *
     * @param id the id
     * @return the pae by id
     */
    public Pae getPAEById(Long id) {
        return paeRepository.findById(id).orElse(null);
    }

    /**
     * Delete pae.
     *
     * @param id the id
     */
    public void deletePAE(Long id) {
        paeRepository.deleteById(id);
    }

    /**
     * Add pae to student pae.
     *
     * @param student the student
     * @param pae     the pae
     * @return the pae
     */
    public Pae addPAEToStudent(Student student, Pae pae) {
        pae.setStudent(student);
        Pae savedPae = paeRepository.save(pae);
        student.setActuelPAE(savedPae);
        studentRepository.save(student);
        return savedPae;
    }


    /**
     * Add existing pae to student pae.
     *
     * @param student the student
     * @param pae     the pae
     * @return the pae
     */
    public Pae addExistingPAEToStudent(Student student, Pae pae) {
        student.setActuelPAE(pae);
        studentRepository.save(student);
        return pae;
    }


}
