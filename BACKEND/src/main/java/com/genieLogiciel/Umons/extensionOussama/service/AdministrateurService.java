package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.auth.service.AbstractLoginService;
import com.genieLogiciel.Umons.auth.service.AuthService;
import com.genieLogiciel.Umons.auth.service.EmailDomain;
import com.genieLogiciel.Umons.extensionOussama.model.Administrateur;
import com.genieLogiciel.Umons.extensionOussama.repository.AdministrateurRepository;
import com.genieLogiciel.Umons.model.Personnel;
import com.genieLogiciel.Umons.repository.PersonnelRepository;
import com.genieLogiciel.Umons.service.UserService;
import com.genieLogiciel.Umons.util.PersonnelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The AdministrateurService class is responsible for managing administrative tasks related to administrators.
 * It extends the AbstractLoginService class, which provides a common base for authentication-related operations.
 */
@Service
public class AdministrateurService extends AbstractLoginService {

    @Autowired private AdministrateurRepository administrateurRepository;
    @Autowired private PersonnelRepository personnelRepository;

    private final UserService userService;
    private final PersonnelMapper personnelMapper;

    /**
     * Constructs an AdministrateurService instance with the specified dependencies.
     *
     * @param authService        The AuthService instance used for authentication-related operations.
     * @param userService        The UserService instance used for user-related operations.
     * @param personnelMapper    The PersonnelMapper instance used for mapping administrators to personnel.
     */
    public AdministrateurService(AuthService authService, UserService userService, PersonnelMapper personnelMapper) {
        super(authService);
        this.userService = userService;
        this.personnelMapper = personnelMapper;
    }

    /**
     * Adds a new administrator to the system.
     *
     * @param newAdmin   The new administrator to be added.
     * @return           A ResponseEntity containing the result of the operation.
     *                   If successful, the status code is OK (200).
     *                   If the admin already exists, the status code is BAD_REQUEST (400).
     */
    public ResponseEntity<String> addAdmin(Administrateur newAdmin) {
        ResponseEntity<String> response = userService.addUser(newAdmin, EmailDomain.ADMIN);
        if (response.getStatusCode() == HttpStatus.OK) {
            administrateurRepository.save(newAdmin);
            personnelMapper.mapToPersonnel(newAdmin);
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return new ResponseEntity<>("Admin already exists", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Retrieves an administrator by their ID.
     *
     * @param ID   The ID of the administrator to be retrieved.
     * @return     A ResponseEntity containing the administrator if found, or null if not found.
     *             The status code is OK (200) if the administrator is found, or BAD_REQUEST (400) if not found.
     */
    public ResponseEntity<Administrateur> getAdminById(Long ID) {
        Optional<Administrateur> administrateurOptional = administrateurRepository.findById(ID);
        if (administrateurOptional.isPresent()) {
            Administrateur administrateur = administrateurOptional.get();
            return new ResponseEntity<>(administrateur, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves all administrators.
     *
     * @return   A list of all administrators.
     */
    public List<Administrateur> getAllAdmin() {
        return administrateurRepository.findAll();
    }

    /**
     * Deletes an administrator by their ID.
     *
     * @param matricule   The ID of the administrator to be deleted.
     * @return            A ResponseEntity containing the result of the operation.
     *                    If successful, the status code is OK (200).
     *                    If the administrator is not found, the status code is BAD_REQUEST (400).
     */
    public ResponseEntity<String> deleteAdminById(Long matricule) {
        Optional<Administrateur> administrateurOptional = administrateurRepository.findById(matricule);
        if (administrateurOptional.isPresent()) {
            Administrateur administrateur = administrateurOptional.get();
            // Check if the admin exists in the list of personnel
            Optional<Personnel> personnelOptional = personnelRepository.findById(matricule);
            if (personnelOptional.isPresent()) {
                Personnel personnel = personnelOptional.get();
                personnelRepository.delete(personnel); // Remove the admin from the list of personnel
            }
            administrateurRepository.deleteById(matricule); // Remove the admin from the admin database
            return new ResponseEntity<>("delete success", HttpStatus.OK);
        }
        return new ResponseEntity<>("admin not found", HttpStatus.BAD_REQUEST);
    }

    /**
     * Authenticates an administrator by their matricule (ID) and password.
     *
     * @param matricule   The matricule (ID) of the administrator.
     * @param password    The password of the administrator.
     * @return            A ResponseEntity containing the result of the authentication.
     *                    If successful, the status code is OK (200).
     *                    If the password is invalid, the status code is UNAUTHORIZED (401).
     *                    If the administrator is not found, the status code is NOT_FOUND (404).
     */
    @Override
    protected ResponseEntity<String> authenticate(Long matricule, String password) {
        System.out.println("logged in admin matricule " + matricule);
        Optional<Administrateur> administrateurOptional = administrateurRepository.findById(matricule);
        if (administrateurOptional.isPresent()) {
            Administrateur administrateur = administrateurOptional.get();
            if (administrateur.getPassword().equals(password)) {
                return ResponseEntity.ok().build(); // Login successful
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Invalid password
            }
        }
        return ResponseEntity.notFound().build(); // Admin not found
    }

    /**
     * Updates the address of an administrator.
     *
     * @param adminId     The ID of the administrator.
     * @param newAddress  The new address to be set.
     * @return            A ResponseEntity containing the result of the operation.
     *                    If successful, the status code is OK (200).
     *                    If the administrator is not found, the status code is NOT_FOUND (404).
     */
    public ResponseEntity<String> updateAddress(Long adminId, String newAddress) {
        Optional<Administrateur> optionalAdmin = administrateurRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            Administrateur admin = optionalAdmin.get();
            admin.setAdresse(newAddress);
            administrateurRepository.save(admin);
            return new ResponseEntity<>("Address updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Admin not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the phone number of an administrator.
     *
     * @param adminId        The ID of the administrator.
     * @param newPhoneNumber The new phone number to be set.
     * @return               A ResponseEntity containing the result of the operation.
     *                       If successful, the status code is OK (200).
     *                       If the administrator is not found, the status code is NOT_FOUND (404).
     */
    public ResponseEntity<String> updatePhoneNumber(Long adminId, Long newPhoneNumber) {
        Optional<Administrateur> optionalAdmin = administrateurRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            Administrateur admin = optionalAdmin.get();
            admin.setNumero(newPhoneNumber);
            administrateurRepository.save(admin);
            return new ResponseEntity<>("Phone number updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Admin not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the password of an administrator.
     *
     * @param adminId     The ID of the administrator.
     * @param newPassword The new password to be set.
     * @return            A ResponseEntity containing the result of the operation.
     *                    If successful, the status code is OK (200).
     *                    If the administrator is not found, the status code is NOT_FOUND (404).
     */
    public ResponseEntity<String> updatePassword(Long adminId, String newPassword) {
        Optional<Administrateur> optionalAdmin = administrateurRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            Administrateur admin = optionalAdmin.get();
            //admin.setPassword(passwordEncoder.encode(newPassword));
            administrateurRepository.save(admin);
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Admin not found", HttpStatus.NOT_FOUND);
        }
    }
}