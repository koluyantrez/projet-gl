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

/**
 * RoomController class
 * This class handles the REST API endpoints for managing rooms.
 */
@RestController
@RequestMapping("/api/rooms")
@CrossOrigin("http://localhost:8080")
public class RoomController {

    @Autowired
    private RoomService roomService;

    /**
     * Get the details of a room by its ID.
     *
     * @param id The ID of the room.
     * @return ResponseEntity containing the room details or a 404 Not Found response if the room is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomDetails(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomDetails(id);
        return room.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Get a list of all rooms.
     *
     * @return List of rooms.
     */
    @GetMapping
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    /**
     * Get a list of rooms filtered by building and/or type.
     *
     * @param building The name of the building to filter by (optional).
     * @param type     The type of the room to filter by (optional).
     * @return List of rooms matching the filter criteria.
     */
    @GetMapping("/filtre")
    public List<Room> getRooms(@RequestParam(required = false) String building, @RequestParam(required = false) String type) {
        return roomService.getRooms(building, type);
    }

    /**
     * Add new equipment to a room by its ID.
     *
     * @param id          The ID of the room.
     * @param equipments  The list of equipment to be added.
     * @return ResponseEntity containing the updated room details or a 404 Not Found response if the room is not found.
     */
    @PostMapping("/{id}/equipments")
    public ResponseEntity<Room> addEquipments(@PathVariable Long id, @RequestBody List<String> equipments) {
        Optional<Room> room = roomService.addEquipments(id, equipments);
        return room.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Add a new room.
     *
     * @param room The room object to be added.
     * @return ResponseEntity containing a success message and the name of the created room, or a 400 Bad Request response if the building is not found or not available.
     */
    @PostMapping()
    public ResponseEntity<Map<String, String>> addNewRoom(@RequestBody Room room) {
        Optional<Room> createdRoom = roomService.addRoom(room);
        Map<String, String> response = new HashMap<>();
        if (createdRoom.isPresent()) {
            response.put("message", "Room added successfully");
            response.put("roomName", createdRoom.get().getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "Building not found or not available");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Get a list of rooms by building name.
     *
     * @param buildingName The name of the building to filter by.
     * @return ResponseEntity containing the list of rooms or a 404 Not Found response if no rooms are found.
     */
    @GetMapping("/building/{buildingName}")
    public ResponseEntity<List<Room>> getRoomsByBuilding(@PathVariable String buildingName) {
        List<Room> rooms = roomService.getRoomByBuilding(buildingName);
        if (rooms != null && !rooms.isEmpty()) {
            return ResponseEntity.ok(rooms);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Update the status of a room by its name.
     *
     * @param roomName The name of the room to update.
     * @param status   The new status of the room.
     * @return ResponseEntity containing the updated room details or a 404 Not Found response if the room is not found.
     */
    @PutMapping("/update/{roomName}/status")
    public ResponseEntity<Room> updateRoomStatus(@PathVariable String roomName, @RequestBody Map<String, String> status) {
        Room room = roomService.updateRoomStatus(roomName, RoomStatus.valueOf(status.get("status")));
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Update the equipment of a room by its name.
     *
     * @param roomName   The name of the room to update.
     * @param equipments The new list of equipment for the room.
     * @return ResponseEntity containing the updated room details or a 404 Not Found response if the room is not found.
     */
    @PutMapping("/update/{roomName}/equipments")
    public ResponseEntity<Room> updateRoomEquipments(@PathVariable String roomName, @RequestBody List<String> equipments) {
        Room room = roomService.updateRoomEquipments(roomName, equipments);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Update the availability of a room by its name.
     *
     * @param roomName    The name of the room to update.
     * @param availability The new availability status of the room.
     * @return ResponseEntity containing the updated room details or a 404 Not Found response if the room is not found.
     */
    @PutMapping("/update/{roomName}/availability")
    public ResponseEntity<Room> updateRoomAvailability(@PathVariable String roomName, @RequestBody Boolean availability) {
        Room room = roomService.updateRoomAvailability(roomName, availability);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Delete a room by its name.
     *
     * @param roomName The name of the room to delete.
     * @return ResponseEntity containing a success message or a 404 Not Found response if the room is not found.
     */
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