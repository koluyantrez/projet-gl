package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Office;
import com.genieLogiciel.Umons.extensionOussama.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping
    public List<Office> getAllOffices() {
        return officeService.getAllOffices();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Office> getOfficeByName(@PathVariable String name) {
        Optional<Office> officeOptional = officeService.getOfficeByName(name);
        return officeOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOffice(@RequestBody Office newOffice) {
        return officeService.addOffice(newOffice);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Office> updateOffice(@PathVariable String name, @RequestBody Office updatedOffice) {
        Office updated = officeService.updateOffice(name, updatedOffice);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteOffice(@PathVariable String name) {
        officeService.deleteOffice(name);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{name}/occupant")
    public ResponseEntity<Office> updateOccupant(
            @PathVariable String name,
            @RequestParam String newOccupant,
            @RequestParam(required = false) Boolean pendingConfirmation) {

        Optional<Office> updated = officeService.updateOccupant(name, newOccupant, pendingConfirmation);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PatchMapping("/{name}/equipment")
    public ResponseEntity<Office> updateEquipment(@PathVariable String name, @RequestBody List<String> newEquipment) {
        Office updated = officeService.updateEquipment(name, newEquipment);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{name}/availability")
    public ResponseEntity<Office> updateAvailability(@PathVariable String name, @RequestParam Boolean availability) {
        Office updated = officeService.updateAvailability(name, availability);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*@PatchMapping("/{name}/delete")
    public ResponseEntity<Office> markOfficeForDeletion(@PathVariable String name) {
        Office office = officeService.markOfficeForDeletion(name);
        if (office != null) {
            return ResponseEntity.ok(office);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @PatchMapping("/{name}/mark-for-deletion")
    public ResponseEntity<String> markOfficeForDeletion(@PathVariable String name) {
        Optional<Office> officeOptional = officeService.getOfficeByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            // Marquer le bureau comme en attente de suppression
            office.setDeletionPending(true);
            officeService.updateOffice(name, office);
            return ResponseEntity.ok("Bureau marqué pour suppression.");
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{name}/confirm-delete")
    public ResponseEntity<Void> confirmOfficeDeletion(@PathVariable String name) {
        officeService.confirmOfficeDeletion(name);
        return ResponseEntity.noContent().build();
    }
}
