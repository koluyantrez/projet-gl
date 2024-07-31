package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.model.RoomStatus;
import com.genieLogiciel.Umons.extensionOussama.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin("http://localhost:8080")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomDetails(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomDetails(id);
        return room.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/filtre")
    public List<Room> getRooms(@RequestParam(required = false) String building, @RequestParam(required = false) String type) {
        return roomService.getRooms(building, type);
    }

    @PostMapping("/{id}/equipments")
    public ResponseEntity<Room> addEquipments(@PathVariable Long id, @RequestBody List<String> equipments) {
        Optional<Room> room = roomService.addEquipments(id, equipments);
        return room.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping()
    public ResponseEntity<Map<String, String>> addNewRoom(@RequestBody Room room) {
        Optional<Room> createdRoom = roomService.addRoom(room);
        Map<String, String> response = new HashMap<>();

        if (createdRoom.isPresent()) {
            response.put("message", "Room added successfully");
            response.put("roomName", createdRoom.get().getName()); // Exemple : ajoutez plus d'infos si nécessaire
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "Building not found or not available");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @GetMapping("/building/{buildingName}")
    public ResponseEntity<List<Room>> getRoomsByBuilding(@PathVariable String buildingName) {
        List<Room> rooms = roomService.getRoomByBuilding(buildingName);
        if (rooms != null && !rooms.isEmpty()) {
            return ResponseEntity.ok(rooms);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update/{roomName}/status")
    public ResponseEntity<Room> updateRoomStatus(@PathVariable String roomName, @RequestBody Map<String, String> status) {
        Room room = roomService.updateRoomStatus(roomName, RoomStatus.valueOf(status.get("status")));
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/update/{roomName}/equipments")
    public ResponseEntity<Room> updateRoomEquipments(@PathVariable String  roomName, @RequestBody List<String> equipments) {
        Room room = roomService.updateRoomEquipments(roomName, equipments);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update/{roomName}/availability")
    public ResponseEntity<Room> updateRoomAvailability(@PathVariable String roomName, @RequestBody Boolean availability) {
        Room room = roomService.updateRoomAvailability(roomName, availability);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{roomName}")
    public ResponseEntity<String> deleteRoomById(@PathVariable String roomName) {
        boolean isDeleted = roomService.deleteRoomByName(roomName);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Room deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
        }
    }
}
