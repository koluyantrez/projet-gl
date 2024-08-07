package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.model.RoomStatus;
import com.genieLogiciel.Umons.extensionOussama.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the RoomController class.
 * This class verifies the behavior of the RoomController methods by mocking the RoomService dependency.
 */
public class RoomControllerTest {

    @InjectMocks
    private RoomController roomController; // The controller being tested.

    @Mock
    private RoomService roomService; // Mocked service used by the controller.

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test.
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for retrieving room details successfully.
     * Verifies that the RoomController returns the correct room details when the RoomService returns a room.
     */
    @Test
    void testGetRoomDetails() {
        Room room = new Room();
        when(roomService.getRoomDetails(anyLong())).thenReturn(Optional.of(room));

        ResponseEntity<Room> response = roomController.getRoomDetails(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(room, response.getBody());
    }

    /**
     * Test case for retrieving room details when room is not found.
     * Verifies that the RoomController returns a NOT_FOUND status when the RoomService returns an empty result.
     */
    @Test
    void testGetRoomDetailsNotFound() {
        when(roomService.getRoomDetails(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Room> response = roomController.getRoomDetails(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test case for retrieving all rooms.
     * Verifies that the RoomController returns a list of rooms as expected when the RoomService provides a list of rooms.
     */
    @Test
    void testGetAllRooms() {
        Room room1 = new Room();
        Room room2 = new Room();
        List<Room> rooms = Arrays.asList(room1, room2);
        when(roomService.getAllRooms()).thenReturn(rooms);

        List<Room> response = roomController.getAllRooms();

        assertEquals(2, response.size());
        assertEquals(rooms, response);
    }

    /**
     * Test case for retrieving rooms with filters applied.
     * Verifies that the RoomController returns a list of rooms matching the filters provided.
     */
    @Test
    void testGetRoomsWithFilters() {
        Room room1 = new Room();
        Room room2 = new Room();
        List<Room> rooms = Arrays.asList(room1, room2);
        when(roomService.getRooms(anyString(), anyString())).thenReturn(rooms);

        List<Room> response = roomController.getRooms("Building1", "Type1");

        assertEquals(2, response.size());
        assertEquals(rooms, response);
    }

    /**
     * Test case for adding new equipment to a room.
     * Verifies that the RoomController successfully adds equipment to a room when the RoomService returns the updated room.
     */
    @Test
    void testAddEquipments() {
        Room room = new Room();
        when(roomService.addEquipments(anyLong(), anyList())).thenReturn(Optional.of(room));

        ResponseEntity<Room> response = roomController.addEquipments(1L, Arrays.asList("Projector", "Whiteboard"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(room, response.getBody());
    }

    /**
     * Test case for adding equipment to a room when the room is not found.
     * Verifies that the RoomController returns a NOT_FOUND status when the RoomService cannot find the room.
     */
    @Test
    void testAddEquipmentsNotFound() {
        when(roomService.addEquipments(anyLong(), anyList())).thenReturn(Optional.empty());

        ResponseEntity<Room> response = roomController.addEquipments(1L, Arrays.asList("Projector", "Whiteboard"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test case for adding a new room.
     * Verifies that the RoomController returns a CREATED status and success message when a room is successfully added.
     */
    @Test
    void testAddNewRoom() {
        Room room = new Room();
        room.setName("Room1");
        when(roomService.addRoom(any(Room.class))).thenReturn(Optional.of(room));

        ResponseEntity<Map<String, String>> response = roomController.addNewRoom(room);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Room added successfully", response.getBody().get("message"));
        assertEquals("Room1", response.getBody().get("roomName"));
    }

    /**
     * Test case for adding a new room when the building is not found.
     * Verifies that the RoomController returns a BAD_REQUEST status and appropriate error message when the room cannot be added.
     */
    @Test
    void testAddNewRoomBuildingNotFound() {
        Room room = new Room();
        when(roomService.addRoom(any(Room.class))).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = roomController.addNewRoom(room);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Building not found or not available", response.getBody().get("message"));
    }

    /**
     * Test case for retrieving rooms by building.
     * Verifies that the RoomController returns a list of rooms for a specific building when the RoomService provides them.
     */
    @Test
    void testGetRoomsByBuilding() {
        Room room1 = new Room();
        Room room2 = new Room();
        List<Room> rooms = Arrays.asList(room1, room2);
        when(roomService.getRoomByBuilding(anyString())).thenReturn(rooms);

        ResponseEntity<List<Room>> response = roomController.getRoomsByBuilding("Building1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rooms, response.getBody());
    }

    /**
     * Test case for retrieving rooms by building when no rooms are found.
     * Verifies that the RoomController returns a NOT_FOUND status when no rooms are available for the given building.
     */
    @Test
    void testGetRoomsByBuildingNotFound() {
        when(roomService.getRoomByBuilding(anyString())).thenReturn(Arrays.asList());

        ResponseEntity<List<Room>> response = roomController.getRoomsByBuilding("Building1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test case for updating the status of a room.
     * Verifies that the RoomController successfully updates the room status when the RoomService returns the updated room.
     */
    @Test
    void testUpdateRoomStatus() {
        Room room = new Room();
        when(roomService.updateRoomStatus(anyString(), any(RoomStatus.class))).thenReturn(room);

        ResponseEntity<Room> response = roomController.updateRoomStatus("Room1", Map.of("status", "AVAILABLE"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(room, response.getBody());
    }

    /**
     * Test case for updating the status of a room when the room is not found.
     * Verifies that the RoomController returns a NOT_FOUND status when the RoomService cannot find the room to update.
     */
    @Test
    void testUpdateRoomStatusNotFound() {
        when(roomService.updateRoomStatus(anyString(), any(RoomStatus.class))).thenReturn(null);

        ResponseEntity<Room> response = roomController.updateRoomStatus("Room1", Map.of("status", "AVAILABLE"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test case for updating the equipment in a room.
     * Verifies that the RoomController successfully updates the room's equipment when the RoomService returns the updated room.
     */
    @Test
    void testUpdateRoomEquipments() {
        Room room = new Room();
        when(roomService.updateRoomEquipments(anyString(), anyList())).thenReturn(room);

        ResponseEntity<Room> response = roomController.updateRoomEquipments("Room1", Arrays.asList("Projector", "Whiteboard"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(room, response.getBody());
    }

    /**
     * Test case for updating the equipment in a room when the room is not found.
     * Verifies that the RoomController returns a NOT_FOUND status when the RoomService cannot find the room to update.
     */
    @Test
    void testUpdateRoomEquipmentsNotFound() {
        when(roomService.updateRoomEquipments(anyString(), anyList())).thenReturn(null);

        ResponseEntity<Room> response = roomController.updateRoomEquipments("Room1", Arrays.asList("Projector", "Whiteboard"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test case for updating the availability of a room.
     * Verifies that the RoomController successfully updates the room's availability when the RoomService returns the updated room.
     */
    @Test
    void testUpdateRoomAvailability() {
        Room room = new Room();
        when(roomService.updateRoomAvailability(anyString(), anyBoolean())).thenReturn(room);

        ResponseEntity<Room> response = roomController.updateRoomAvailability("Room1", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(room, response.getBody());
    }

    /**
     * Test case for updating the availability of a room when the room is not found.
     * Verifies that the RoomController returns a NOT_FOUND status when the RoomService cannot find the room to update.
     */
    @Test
    void testUpdateRoomAvailabilityNotFound() {
        when(roomService.updateRoomAvailability(anyString(), anyBoolean())).thenReturn(null);

        ResponseEntity<Room> response = roomController.updateRoomAvailability("Room1", true);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test case for deleting a room by its name.
     * Verifies that the RoomController returns a NO_CONTENT status and success message when the room is successfully deleted.
     */
    @Test
    void testDeleteRoomById() {
        when(roomService.deleteRoomByName(anyString())).thenReturn(true);

        ResponseEntity<String> response = roomController.deleteRoomById("Room1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Room deleted successfully", response.getBody());
    }

    /**
     * Test case for deleting a room by its name when the room is not found.
     * Verifies that the RoomController returns a NOT_FOUND status and error message when the room cannot be found for deletion.
     */
    @Test
    void testDeleteRoomByIdNotFound() {
        when(roomService.deleteRoomByName(anyString())).thenReturn(false);

        ResponseEntity<String> response = roomController.deleteRoomById("Room1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Room not found", response.getBody());
    }
}
