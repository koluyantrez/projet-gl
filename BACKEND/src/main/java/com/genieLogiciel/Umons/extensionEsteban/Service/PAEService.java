package com.genieLogiciel.Umons.extensionEsteban.Service;


import com.genieLogiciel.Umons.extensionEsteban.Repository.PAERepository;
import com.genieLogiciel.Umons.extensionEsteban.model.Pae;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PAEService {

    @Autowired
    private PAERepository paeRepository;

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
}
