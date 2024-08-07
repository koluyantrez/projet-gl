package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Office;
import com.genieLogiciel.Umons.extensionOussama.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling office-related operations.
 */
@RestController
@RequestMapping("/api/offices")
@CrossOrigin("http://localhost:8080")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    /**
     * Retrieves all offices.
     *
     * @return List of all offices
     */
    @GetMapping
    public List<Office> getAllOffices() {
        return officeService.getAllOffices();
    }

    /**
     * Retrieves an office by its name.
     *
     * @param name the name of the office to retrieve
     * @return the office if found, otherwise a 404 (Not Found) response
     */
    @GetMapping("/{name}")
    public ResponseEntity<Office> getOfficeByName(@PathVariable String name) {
        Optional<Office> officeOptional = officeService.getOfficeByName(name);
        return officeOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Adds a new office.
     *
     * @param newOffice the new office to be added
     * @return a 200 (OK) response with a success message, or a 400 (Bad Request) response if the office could not be added
     */
    @PostMapping("/add")
    public ResponseEntity<String> addOffice(@RequestBody Office newOffice) {
        return officeService.addOffice(newOffice);
    }

    /**
     * Updates an existing office.
     *
     * @param name           the name of the office to update
     * @param updatedOffice the updated office information
     * @return the updated office if found, otherwise a 404 (Not Found) response
     */
    @PutMapping("/{name}")
    public ResponseEntity<Office> updateOffice(@PathVariable String name, @RequestBody Office updatedOffice) {
        Office updated = officeService.updateOffice(name, updatedOffice);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an office.
     *
     * @param name the name of the office to delete
     * @return a 204 (No Content) response
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteOffice(@PathVariable String name) {
        officeService.deleteOffice(name);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the occupant of an office.
     *
     * @param name               the name of the office to update
     * @param newOccupant        the new occupant for the office
     * @param pendingConfirmation whether the new occupant is pending confirmation
     * @return the updated office if found, otherwise a 404 (Not Found) response
     */
    @PatchMapping("/{name}/occupant")
    public ResponseEntity<Office> updateOccupant(
            @PathVariable String name,
            @RequestParam String newOccupant,
            @RequestParam(required = false) Boolean pendingConfirmation) {

        Optional<Office> updated = officeService.updateOccupant(name, newOccupant, pendingConfirmation);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates the equipment of an office.
     *
     * @param name          the name of the office to update
     * @param newEquipment   the new equipment for the office
     * @return the updated office if found, otherwise a 404 (Not Found) response
     */
    @PatchMapping("/{name}/equipment")
    public ResponseEntity<Office> updateEquipment(@PathVariable String name, @RequestBody List<String> newEquipment) {
        Office updated = officeService.updateEquipment(name, newEquipment);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates the availability of an office.
     *
     * @param name        the name of the office to update
     * @param availability the new availability status for the office
     * @return the updated office if found, otherwise a 404 (Not Found) response
     */
    @PatchMapping("/{name}/availability")
    public ResponseEntity<Office> updateAvailability(@PathVariable String name, @RequestParam Boolean availability) {
        Office updated = officeService.updateAvailability(name, availability);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Marks an office for deletion.
     *
     * @param name the name of the office to mark for deletion
     * @return a 200 (OK) response with a success message, or a 404 (Not Found) response if the office is not found
     */
    @PatchMapping("/{name}/mark-for-deletion")
    public ResponseEntity<String> markOfficeForDeletion(@PathVariable String name) {
        Optional<Office> officeOptional = officeService.getOfficeByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            office.setDeletionPending(true);
            officeService.updateOffice(name, office);
            return ResponseEntity.ok("Office marked for deletion.");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Confirms the deletion of an office.
     *
     * @param name the name of the office to delete
     * @return a 204 (No Content) response
     */
    @DeleteMapping("/{name}/confirm-delete")
    public ResponseEntity<Void> confirmOfficeDeletion(@PathVariable String name) {
        officeService.confirmOfficeDeletion(name);
        return ResponseEntity.noContent().build();
    }
}