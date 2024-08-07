package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Administrateur;
import com.genieLogiciel.Umons.extensionOussama.service.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The AdministrateurController class is a RESTful controller that provides endpoints for managing administrators.
 * It uses the AdministrateurService to handle the business logic.
 */
@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/admin")
public class AdministrateurController {

    @Autowired private AdministrateurService administrateurService;

    /**
     * Adds a new administrator.
     *
     * @param newAdmin the new administrator to be added
     * @return a ResponseEntity containing a success or error message
     */
    @PostMapping("/addNew")
    public ResponseEntity<String> addAdmin(@RequestBody Administrateur newAdmin){
        return administrateurService.addAdmin(newAdmin);
    }

    /**
     * Retrieves an administrator by ID.
     *
     * @param id the ID of the administrator to be retrieved
     * @return a ResponseEntity containing the retrieved administrator
     */
    @GetMapping("/{id}")
    public ResponseEntity<Administrateur> getAdminById(@PathVariable Long id){
        return administrateurService.getAdminById(id);
    }

    /**
     * Retrieves all administrators.
     *
     * @return a list of all administrators
     */
    @GetMapping("/All")
    public List<Administrateur> getAllAdmin(){
        return administrateurService.getAllAdmin();
    }

    /**
     * Deletes an administrator by ID.
     *
     * @param id the ID of the administrator to be deleted
     * @return a ResponseEntity containing a success or error message
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAdminById(@PathVariable Long id){
        return administrateurService.deleteAdminById(id);
    }

    /**
     * Updates the address of an administrator.
     *
     * @param adminId the ID of the administrator to be updated
     * @param newAddress the new address to be set
     * @return a ResponseEntity containing a success or error message
     */
    @PatchMapping("/{adminId}/address")
    public ResponseEntity<String> updateAddress(@PathVariable Long adminId, @RequestBody String newAddress) {
        return administrateurService.updateAddress(adminId, newAddress);
    }

    /**
     * Updates the phone number of an administrator.
     *
     * @param adminId the ID of the administrator to be updated
     * @param newPhoneNumber the new phone number to be set
     * @return a ResponseEntity containing a success or error message
     */
    @PatchMapping("/{adminId}/phone")
    public ResponseEntity<String> updatePhoneNumber(@PathVariable Long adminId, @RequestBody Long newPhoneNumber) {
        return administrateurService.updatePhoneNumber(adminId, newPhoneNumber);
    }

    /**
     * Updates the password of an administrator.
     *
     * @param adminId the ID of the administrator to be updated
     * @param newPassword the new password to be set
     * @return a ResponseEntity containing a success or error message
     */
    @PatchMapping("/{adminId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long adminId, @RequestBody String newPassword) {
        return administrateurService.updatePassword(adminId, newPassword);
    }
}