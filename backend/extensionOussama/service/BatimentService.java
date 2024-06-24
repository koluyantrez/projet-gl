package com.genieLogiciel.Umons.backend.extensionOussama.service;

import com.genieLogiciel.Umons.backend.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.backend.extensionOussama.repository.BatimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.*;
@Service
public class BatimentService {

    @Autowired
    private BatimentRepository batimentRepository;

    public Batiment addBatiment(Batiment batiment) {
        return batimentRepository.save(batiment);
    }

    public Batiment getBatimentById(Long id) throws ChangeSetPersister.NotFoundException {
        return batimentRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public void deleteBatimentById(Long id) {
        batimentRepository.deleteById(id);
    }
}