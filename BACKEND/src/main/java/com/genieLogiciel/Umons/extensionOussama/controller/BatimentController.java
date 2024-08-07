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

@RestController
@RequestMapping("/api/buildings")
@CrossOrigin("http://localhost:8080")
public class BatimentController {

    private final BatimentService batimentService;

    @Autowired
    public BatimentController(BatimentService batimentService) {
        this.batimentService = batimentService;
    }


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

    @PostMapping
    public ResponseEntity<Batiment> addBuilding(@RequestBody Batiment newBuilding) {
        Batiment createdBuilding = batimentService.addBuilding(newBuilding);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuilding);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Batiment> getBuildingById(@PathVariable Long id) {
        try {
            Batiment batiment = batimentService.getBuildingById(id);
            return ResponseEntity.ok(batiment);
        } catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Batiment>> getAllBuildings() {
        List<Batiment> buildings = batimentService.getAllBuildings();
        return ResponseEntity.ok(buildings);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Batiment> getBuildingByName(@PathVariable String name) {
        Optional<Batiment> batiment = batimentService.getBuildingByName(name);
        return batiment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{buildingName}/availability")
    public ResponseEntity<Batiment> updateBuildingAvailability(@PathVariable String buildingName, @RequestBody Boolean availability) {
        Batiment batiment = batimentService.updateBuildingAvailability(buildingName, availability);
        if (batiment != null) {
            return ResponseEntity.ok(batiment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuildingById(@PathVariable Long id) {
        batimentService.deleteBuildingById(id);
        return ResponseEntity.noContent().build();
    }

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
