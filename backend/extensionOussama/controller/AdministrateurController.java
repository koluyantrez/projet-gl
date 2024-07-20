package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Administrateur;
import com.genieLogiciel.Umons.extensionOussama.service.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/admin")
public class AdministrateurController {

    @Autowired private  AdministrateurService administrateurService;

    @PostMapping("/addNew")
    public ResponseEntity<String> addAdmin(@RequestBody Administrateur newAdmin){
        return administrateurService.addAdmin(newAdmin);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrateur> getAdminById(@PathVariable Long id){
        return administrateurService.getAdminById(id);
    }

    @GetMapping("/All")
    public List<Administrateur> getAllAdmin(){
        return administrateurService.getAllAdmin();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAdminById(@PathVariable Long id){
        return administrateurService.deleteAdminById(id);
    }
}
