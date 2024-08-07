package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.model.RoomStatus;
import com.genieLogiciel.Umons.extensionOussama.repository.BatimentRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.OfficeRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the RoomService class.
 * This class tests various scenarios for managing rooms, including adding, retrieving, updating, and deleting rooms.
 */
@SpringBootTest
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository; // Mocked repository for Room entities.

    @Mock
    private BatimentRepository batimentRepository; // Mocked repository for Batiment entities.

    @Mock
    private BatimentService batimentService; // Mocked service for managing Batiment entities.

    @Mock
    private OfficeRepository officeRepository; // Mocked repository for Office entities.

    @InjectMocks
    private RoomService roomService; // The service being tested.

    public RoomServiceTest() {
        // Initialize mocks before each test.
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for adding a room successfully.
     * Verifies that the room is saved if the associated building exists and the room is correctly added.
     */
    /*@Test
    public void testAddRoom_success() {
        Room newRoom = new Room();
        newRoom.setId(1L);
        newRoom.setAssociatedBuilding("Building1");
        newRoom.setType("OFFICE");
        newRoom.setAvailable(true);

        Batiment batiment = new Batiment();
        batiment.setAvailable(true);
        batiment.setAdresse("Address1");

        when(batimentRepository.findByName("Building1")).thenReturn(Optional.of(batiment));
        when(roomRepository.save(newRoom)).thenReturn(newRoom);
        when(batimentService.addRoomToBuild(newRoom.getName())).thenReturn(true);

        Optional<Room> result = roomService.addRoom(newRoom);

        assertTrue(result.isPresent());
        assertEquals(newRoom, result.get());
        verify(roomRepository, times(2)).save(newRoom);
        verify(batimentRepository).save(batiment);
        verify(officeRepository).save(any(Office.class));
    }*/

    /**
     * Test case for adding a room when the associated building is not found.
     * Verifies that the room is not saved if the associated building does not exist.
     */
    @Test
    public void testAddRoom_buildingNotFound() {
        Room newRoom = new Room();
        newRoom.setAssociatedBuilding("UnknownBuilding");

        when(batimentRepository.findByName("UnknownBuilding")).thenReturn(Optional.empty());

        Optional<Room> result = roomService.addRoom(newRoom);

        assertFalse(result.isPresent());
        verify(roomRepository, never()).save(any(Room.class));
    }

    /**
     * Test case for retrieving room details when the room is not found.
     * Verifies that no room details are returned if the room with the specified ID does not exist.
     */
    @Test
    public void testGetRoomDetails_notFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Room> result = roomService.getRoomDetails(1L);

        assertFalse(result.isPresent());
    }

    /**
     * Test case for adding equipment to a room when the room is not found.
     * Verifies that no equipment is added if the room with the specified ID does not exist.
     */
    @Test
    public void testAddEquipments_roomNotFound() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Room> result = roomService.addEquipments(1L, Arrays.asList("Whiteboard"));

        assertFalse(result.isPresent());
    }

    /**
     * Test case for updating the status of a room when the room is not found.
     * Verifies that no room status is updated if the room with the specified name does not exist.
     */
    @Test
    public void testUpdateRoomStatus_roomNotFound() {
        when(roomRepository.getRoomByName("NonExistentRoom")).thenReturn(Optional.empty());

        Room updatedRoom = roomService.updateRoomStatus("NonExistentRoom", RoomStatus.AVAILABLE);

        assertNull(updatedRoom);
    }

    /**
     * Test case for deleting a room by name when the room is not found.
     * Verifies that no room is deleted if the room with the specified name does not exist.
     */
    @Test
    public void testDeleteRoomByName_notFound() {
        when(roomRepository.getRoomByName("NonExistentRoom")).thenReturn(Optional.empty());

        boolean result = roomService.deleteRoomByName("NonExistentRoom");

        assertFalse(result);
        verify(roomRepository, never()).deleteByName("NonExistentRoom");
    }
}
