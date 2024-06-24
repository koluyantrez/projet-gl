package com.genieLogiciel.Umons.backend.extensionOussama.controller;

import com.genieLogiciel.Umons.backend.extensionOussama.model.Administrateur;
import com.genieLogiciel.Umons.backend.extensionOussama.service.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
public class AdministrateurController {

    @Autowired private  AdministrateurService administrateurService;

    @PostMapping("/admin/addNew")
    public ResponseEntity<String> addAdmin(@RequestBody Administrateur newAdmin){
        return administrateurService.addAdmin(newAdmin);
    }

    @GetMapping("/adminById")
    public ResponseEntity<Administrateur> getAdminById(@RequestParam Long id){
        return administrateurService.getAdminById(id);
    }

    @GetMapping("/AllAdmin")
    public List<Administrateur> getAllAdmin(){
        return administrateurService.getAllAdmin();
    }

    @DeleteMapping("/deleteAdminById")
    public ResponseEntity<String> deleteAdminById(@RequestParam Long id){
        return administrateurService.deleteAdminById(id);
    }
}
