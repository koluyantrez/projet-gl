package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Office;
import com.genieLogiciel.Umons.extensionOussama.service.OfficeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * This class is responsible for testing the OfficeController class, which is responsible for handling HTTP requests
 * related to the management of offices in the application.
 */
public class OfficeControllerTest {

    /**
     * The instance of the OfficeController class that will be tested.
     */
    @InjectMocks
    private OfficeController officeController;

    /**
     * The mock instance of the OfficeService class, which will be used to simulate the behavior of the service layer.
     */
    @Mock
    private OfficeService officeService;

    /**
     * This method is executed before each test and is responsible for initializing the Mockito annotations.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * This test verifies that the OfficeController can successfully retrieve all the offices.
     */
    @Test
    void testGetAllOffices() {
        Office office1 = new Office();
        Office office2 = new Office();
        List<Office> offices = Arrays.asList(office1, office2);
        when(officeService.getAllOffices()).thenReturn(offices);

        List<Office> response = officeController.getAllOffices();

        assertEquals(2, response.size());
        assertEquals(offices, response);
    }

    /**
     * This test verifies that the OfficeController can successfully retrieve an office by its name.
     */
    @Test
    void testGetOfficeByName() {
        Office office = new Office();
        when(officeService.getOfficeByName(anyString())).thenReturn(Optional.of(office));

        ResponseEntity<Office> response = officeController.getOfficeByName("Office1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(office, response.getBody());
    }

    /**
     * This test verifies that the OfficeController returns a 404 Not Found response when an office is not found by its name.
     */
    @Test
    void testGetOfficeByNameNotFound() {
        when(officeService.getOfficeByName(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Office> response = officeController.getOfficeByName("Office1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * This test verifies that the OfficeController can successfully add a new office.
     */
    @Test
    void testAddOffice() {
        when(officeService.addOffice(any(Office.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Office added"));

        Office newOffice = new Office();
        ResponseEntity<String> response = officeController.addOffice(newOffice);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Office added", response.getBody());
    }

    /**
     * This test verifies that the OfficeController can successfully update an existing office.
     */
    @Test
    void testUpdateOffice() {
        Office updatedOffice = new Office();
        when(officeService.updateOffice(anyString(), any(Office.class))).thenReturn(updatedOffice);

        ResponseEntity<Office> response = officeController.updateOffice("Office1", updatedOffice);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOffice, response.getBody());
    }

    /**
     * This test verifies that the OfficeController returns a 404 Not Found response when an office is not found for update.
     */
    @Test
    void testUpdateOfficeNotFound() {
        when(officeService.updateOffice(anyString(), any(Office.class))).thenReturn(null);

        Office updatedOffice = new Office();
        ResponseEntity<Office> response = officeController.updateOffice("Office1", updatedOffice);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * This test verifies that the OfficeController can successfully delete an office.
     */
    @Test
    void testDeleteOffice() {
        doNothing().when(officeService).deleteOffice(anyString());

        ResponseEntity<Void> response = officeController.deleteOffice("Office1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * This test verifies that the OfficeController can successfully update the occupant of an office.
     */
    @Test
    void testUpdateOccupant() {
        Office updatedOffice = new Office();
        when(officeService.updateOccupant(anyString(), anyString(), anyBoolean())).thenReturn(Optional.of(updatedOffice));

        ResponseEntity<Office> response = officeController.updateOccupant("Office1", "NewOccupant", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOffice, response.getBody());
    }

    /**
     * This test verifies that the OfficeController returns a 404 Not Found response when an office is not found for updating the occupant.
     */
    @Test
    void testUpdateOccupantNotFound() {
        when(officeService.updateOccupant(anyString(), anyString(), anyBoolean())).thenReturn(Optional.empty());

        ResponseEntity<Office> response = officeController.updateOccupant("Office1", "NewOccupant", true);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * This test verifies that the OfficeController can successfully update the equipment of an office.
     */
    @Test
    void testUpdateEquipment() {
        Office updatedOffice = new Office();
        when(officeService.updateEquipment(anyString(), anyList())).thenReturn(updatedOffice);

        List<String> newEquipment = Arrays.asList("Projector", "Whiteboard");
        ResponseEntity<Office> response = officeController.updateEquipment("Office1", newEquipment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOffice, response.getBody());
    }

    /**
     * This test verifies that the OfficeController returns a 404 Not Found response when an office is not found for updating the equipment.
     */
    @Test
    void testUpdateEquipmentNotFound() {
        when(officeService.updateEquipment(anyString(), anyList())).thenReturn(null);

        List<String> newEquipment = Arrays.asList("Projector", "Whiteboard");
        ResponseEntity<Office> response = officeController.updateEquipment("Office1", newEquipment);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * This test verifies that the OfficeController can successfully update the availability of an office.
     */
    @Test
    void testUpdateAvailability() {
        Office updatedOffice = new Office();
        when(officeService.updateAvailability(anyString(), anyBoolean())).thenReturn(updatedOffice);

        ResponseEntity<Office> response = officeController.updateAvailability("Office1", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOffice, response.getBody());
    }

    /**
     * This test verifies that the OfficeController returns a 404 Not Found response when an office is not found for updating the availability.
     */
    @Test
    void testUpdateAvailabilityNotFound() {
        when(officeService.updateAvailability(anyString(), anyBoolean())).thenReturn(null);

        ResponseEntity<Office> response = officeController.updateAvailability("Office1", true);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * This test verifies that the OfficeController returns a 404 Not Found response when an office is not found for marking it for deletion.
     */
    @Test
    void testMarkOfficeForDeletionNotFound() {
        when(officeService.getOfficeByName(anyString())).thenReturn(Optional.empty());

        ResponseEntity<String> response = officeController.markOfficeForDeletion("Office1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * This test verifies that the OfficeController can successfully confirm the deletion of an office.
     */
    @Test
    void testConfirmOfficeDeletion() {
        doNothing().when(officeService).confirmOfficeDeletion(anyString());

        ResponseEntity<Void> response = officeController.confirmOfficeDeletion("Office1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}