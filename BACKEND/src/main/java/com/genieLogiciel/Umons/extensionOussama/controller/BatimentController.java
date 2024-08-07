package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.service.BatimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The BatimentController class is a Spring REST controller that handles CRUD operations for building management.
 */
@RestController
@RequestMapping("/api/buildings")
@CrossOrigin("http://localhost:8080")
public class BatimentController {

    private final BatimentService batimentService;

    @Autowired
    public BatimentController(BatimentService batimentService) {
        this.batimentService = batimentService;
    }

    /**
     * Adds a new room to an existing building.
     *
     * @param roomName the name of the room to be added
     * @return a ResponseEntity with the status and a message indicating the result of the operation
     */
    @PostMapping("/{roomName}")
    public ResponseEntity<String> addRoomToBuild(@PathVariable String roomName) {
        Optional<String> result = batimentService.addRoomToBuild(roomName);
        if (result.isPresent()) {
            String message = result.get();
            if ("Room added to building successfully".equals(message)) {
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
    }

    /**
     * Adds a new building to the system.
     *
     * @param newBuilding the new building to be added
     * @return a ResponseEntity with the created building and a status of CREATED
     */
    @PostMapping
    public ResponseEntity<Batiment> addBuilding(@RequestBody Batiment newBuilding) {
        Batiment createdBuilding = batimentService.addBuilding(newBuilding);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuilding);
    }

    /**
     * Retrieves a building by its ID.
     *
     * @param id the ID of the building to be retrieved
     * @return a ResponseEntity with the requested building or a NOT_FOUND status if the building is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Batiment> getBuildingById(@PathVariable Long id) {
        try {
            Batiment batiment = batimentService.getBuildingById(id);
            return ResponseEntity.ok(batiment);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Retrieves all buildings.
     *
     * @return a ResponseEntity with the list of all buildings
     */
    @GetMapping
    public ResponseEntity<List<Batiment>> getAllBuildings() {
        List<Batiment> buildings = batimentService.getAllBuildings();
        return ResponseEntity.ok(buildings);
    }

    /**
     * Retrieves a building by its name.
     *
     * @param name the name of the building to be retrieved
     * @return a ResponseEntity with the requested building or a NOT_FOUND status if the building is not found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Batiment> getBuildingByName(@PathVariable String name) {
        Optional<Batiment> batiment = batimentService.getBuildingByName(name);
        return batiment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates the availability of a building.
     *
     * @param buildingName the name of the building to be updated
     * @param availability the new availability status of the building
     * @return a ResponseEntity with the updated building or a NOT_FOUND status if the building is not found
     */
    @PutMapping("/{buildingName}/availability")
    public ResponseEntity<Batiment> updateBuildingAvailability(@PathVariable String buildingName, @RequestBody Boolean availability) {
        Batiment batiment = batimentService.updateBuildingAvailability(buildingName, availability);
        if (batiment != null) {
            return ResponseEntity.ok(batiment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a building by its ID.
     *
     * @param id the ID of the building to be deleted
     * @return a ResponseEntity with a NO_CONTENT status indicating the successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuildingById(@PathVariable Long id) {
        batimentService.deleteBuildingById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Removes a room from a building.
     *
     * @param roomName the name of the room to be removed
     * @param buildingName the name of the building from which the room should be removed
     * @return a ResponseEntity with a status indicating the result of the operation
     */
    @DeleteMapping("/{buildingName}/rooms/{roomName}")
    public ResponseEntity<String> removeRoomFromBuilding(@PathVariable String roomName, @PathVariable String buildingName) {
        Optional<String> result = batimentService.removeRoomFromBuilding(roomName, buildingName);
        if (result.isPresent()) {
            String message = result.get();
            if ("Remove Success".equals(message)) {
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
    }

    /**
     * Deletes a building by its name.
     *
     * @param name the name of the building to be deleted
     * @return a ResponseEntity with a status indicating the result of the operation
     */
    @DeleteMapping("/name/{name}")
    public ResponseEntity<String> deleteBuildingByName(@PathVariable String name) {
        boolean isDeleted = batimentService.deleteBuildingByName(name);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Building and associated rooms deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Building not found");
        }
    }
}