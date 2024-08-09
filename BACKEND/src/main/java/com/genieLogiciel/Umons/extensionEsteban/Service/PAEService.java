package com.genieLogiciel.Umons.extensionEsteban.Service;


import com.genieLogiciel.Umons.extensionEsteban.Repository.PAERepository;
import com.genieLogiciel.Umons.extensionEsteban.model.Pae;
import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PAEService {

    @Autowired
    private PAERepository paeRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Pae createPAE(Pae pae) {
        return paeRepository.save(pae);
    }

    public List<Pae> getAllPAEs() {
        return paeRepository.findAll();
    }

    public Pae getPAEById(Long id) {
        return paeRepository.findById(id).orElse(null);
    }

    public void deletePAE(Long id) {
        paeRepository.deleteById(id);
    }

    public Pae addPAEToStudent(Student student, Pae pae) {
        pae.setStudent(student);
        Pae savedPae = paeRepository.save(pae);
        student.setActuelPAE(savedPae);
        studentRepository.save(student);
        return savedPae;
    }


    public Pae addExistingPAEToStudent(Student student, Pae pae) {
        student.setActuelPAE(pae);
        studentRepository.save(student);
        return pae;
    }


}
