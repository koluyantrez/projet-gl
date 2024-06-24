package com.genieLogiciel.Umons.backend.extensionOussama.controller;

import com.genieLogiciel.Umons.backend.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.backend.extensionOussama.service.BatimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/batiments")
public class BatimentController {

    @Autowired
    private BatimentService batimentService;

    @PostMapping
    public ResponseEntity<Batiment> addBatiment(@RequestBody Batiment batiment) {
        Batiment savedBatiment = batimentService.addBatiment(batiment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBatiment);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Batiment> getBatimentById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Batiment batiment = batimentService.getBatimentById(id);
        return ResponseEntity.ok(batiment);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBatimentById(@PathVariable Long id) {
        batimentService.deleteBatimentById(id);
        return ResponseEntity.noContent().build();
    }
}