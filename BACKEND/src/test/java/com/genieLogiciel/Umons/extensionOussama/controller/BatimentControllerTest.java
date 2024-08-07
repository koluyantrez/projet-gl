package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.extensionOussama.service.BatimentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * This class represents the unit tests for the BatimentController class, which handles the
 * building-related operations in the application.
 */
public class BatimentControllerTest {

    /**
     * The BatimentController instance to be tested.
     */
    @InjectMocks
    private BatimentController batimentController;

    /**
     * The mock instance of the BatimentService, which the BatimentController depends on.
     */
    @Mock
    private BatimentService batimentService;

    /**
     * Initializes the mock objects before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the addRoomToBuild method of the BatimentController.
     * It verifies that the method returns a successful response with the expected message.
     */
    @Test
    void testAddRoomToBuild() {
        when(batimentService.addRoomToBuild(anyString())).thenReturn(Optional.of("Room added to building successfully"));

        ResponseEntity<String> response = batimentController.addRoomToBuild("Room1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room added to building successfully", response.getBody());
    }

    /**
     * Tests the addBuilding method of the BatimentController.
     * It verifies that the method returns a successful response with the created building.
     */
    @Test
    void testAddBuilding() {
        Batiment building = new Batiment();
        when(batimentService.addBuilding(any(Batiment.class))).thenReturn(building);

        ResponseEntity<Batiment> response = batimentController.addBuilding(building);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(building, response.getBody());
    }

    /**
     * Tests the getBuildingById method of the BatimentController.
     * It verifies that the method returns a successful response with the requested building.
     */
    @Test
    void testGetBuildingById() throws ChangeSetPersister.NotFoundException {
        Batiment building = new Batiment();
        when(batimentService.getBuildingById(anyLong())).thenReturn(building);

        ResponseEntity<Batiment> response = batimentController.getBuildingById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(building, response.getBody());
    }

    /**
     * Tests the getBuildingById method of the BatimentController when the building is not found.
     * It verifies that the method returns a not found response.
     */
    @Test
    void testGetBuildingByIdNotFound() throws ChangeSetPersister.NotFoundException {
        when(batimentService.getBuildingById(anyLong())).thenThrow(ChangeSetPersister.NotFoundException.class);

        ResponseEntity<Batiment> response = batimentController.getBuildingById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    /**
     * Tests the getAllBuildings method of the BatimentController.
     * It verifies that the method returns a successful response with the list of all buildings.
     */
    @Test
    void testGetAllBuildings() {
        Batiment building1 = new Batiment();
        Batiment building2 = new Batiment();
        List<Batiment> buildings = Arrays.asList(building1, building2);
        when(batimentService.getAllBuildings()).thenReturn(buildings);

        ResponseEntity<List<Batiment>> response = batimentController.getAllBuildings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buildings, response.getBody());
    }

    /**
     * Tests the getBuildingByName method of the BatimentController.
     * It verifies that the method returns a successful response with the requested building.
     */
    @Test
    void testGetBuildingByName() {
        Batiment building = new Batiment();
        when(batimentService.getBuildingByName(anyString())).thenReturn(Optional.of(building));

        ResponseEntity<Batiment> response = batimentController.getBuildingByName("Building1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(building, response.getBody());
    }

    /**
     * Tests the getBuildingByName method of the BatimentController when the building is not found.
     * It verifies that the method returns a not found response.
     */
    @Test
    void testGetBuildingByNameNotFound() {
        when(batimentService.getBuildingByName(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Batiment> response = batimentController.getBuildingByName("Building1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests the updateBuildingAvailability method of the BatimentController.
     * It verifies that the method returns a successful response with the updated building.
     */
    @Test
    void testUpdateBuildingAvailability() {
        Batiment building = new Batiment();
        when(batimentService.updateBuildingAvailability(anyString(), anyBoolean())).thenReturn(building);

        ResponseEntity<Batiment> response = batimentController.updateBuildingAvailability("Building1", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(building, response.getBody());
    }

    /**
     * Tests the updateBuildingAvailability method of the BatimentController when the building is not found.
     * It verifies that the method returns a not found response.
     */
    @Test
    void testUpdateBuildingAvailabilityNotFound() {
        when(batimentService.updateBuildingAvailability(anyString(), anyBoolean())).thenReturn(null);

        ResponseEntity<Batiment> response = batimentController.updateBuildingAvailability("Building1", true);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Tests the deleteBuildingById method of the BatimentController.
     * It verifies that the method returns a successful no-content response.
     */
    @Test
    void testDeleteBuildingById() {
        doNothing().when(batimentService).deleteBuildingById(anyLong());

        ResponseEntity<Void> response = batimentController.deleteBuildingById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Tests the removeRoomFromBuilding method of the BatimentController.
     * It verifies that the method returns a successful response with the expected message.
     */
    @Test
    void testRemoveRoomFromBuilding() {
        when(batimentService.removeRoomFromBuilding(anyString(), anyString())).thenReturn(Optional.of("Remove Success"));

        ResponseEntity<String> response = batimentController.removeRoomFromBuilding("Room1", "Building1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Remove Success", response.getBody());
    }

    /**
     * Tests the removeRoomFromBuilding method of the BatimentController when the room is not found.
     * It verifies that the method returns a not found response with the expected message.
     */
    @Test
    void testRemoveRoomFromBuildingNotFound() {
        when(batimentService.removeRoomFromBuilding(anyString(), anyString())).thenReturn(Optional.of("Room not found"));

        ResponseEntity<String> response = batimentController.removeRoomFromBuilding("Room1", "Building1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Room not found", response.getBody());
    }

    /**
     * Tests the deleteBuildingByName method of the BatimentController.
     * It verifies that the method returns a successful no-content response with the expected message.
     */
    @Test
    void testDeleteBuildingByName() {
        when(batimentService.deleteBuildingByName(anyString())).thenReturn(true);

        ResponseEntity<String> response = batimentController.deleteBuildingByName("Building1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Building and associated rooms deleted successfully", response.getBody());
    }

    /**
     * Tests the deleteBuildingByName method of the BatimentController when the building is not found.
     * It verifies that the method returns a not found response with the expected message.
     */
    @Test
    void testDeleteBuildingByNameNotFound() {
        when(batimentService.deleteBuildingByName(anyString())).thenReturn(false);

        ResponseEntity<String> response = batimentController.deleteBuildingByName("Building1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Building not found", response.getBody());
    }
}