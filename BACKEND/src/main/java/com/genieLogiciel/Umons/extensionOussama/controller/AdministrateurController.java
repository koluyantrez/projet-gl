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

    @PatchMapping("/{adminId}/address")
    public ResponseEntity<String> updateAddress(@PathVariable Long adminId, @RequestBody String newAddress) {
        return administrateurService.updateAddress(adminId, newAddress);
    }

    @PatchMapping("/{adminId}/phone")
    public ResponseEntity<String> updatePhoneNumber(@PathVariable Long adminId, @RequestBody Long newPhoneNumber) {
        return administrateurService.updatePhoneNumber(adminId, newPhoneNumber);
    }

    @PatchMapping("/{adminId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long adminId, @RequestBody String newPassword) {
        return administrateurService.updatePassword(adminId, newPassword);
    }
}
