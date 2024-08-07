package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Administrateur;
import com.genieLogiciel.Umons.extensionOussama.service.AdministrateurService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Test class for AdministrateurController
 * This class contains unit tests for the AdministrateurController class, which is responsible for handling HTTP requests related to administrators.
 */
public class AdministrateurControllerTest {

    /**
     * Instance of AdministrateurController to be tested
     */
    @InjectMocks
    private AdministrateurController administrateurController;

    /**
     * Mock instance of AdministrateurService, which is used by the AdministrateurController
     */
    @Mock
    private AdministrateurService administrateurService;

    /**
     * Set up the test environment by initializing the mocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for the addAdmin method, which adds a new administrator.
     * The test checks that the method returns a ResponseEntity with the expected status and body.
     */
    @Test
    void testAddAdmin() {
        Administrateur admin = new Administrateur();
        when(administrateurService.addAdmin(any(Administrateur.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Admin added"));

        ResponseEntity<String> response = administrateurController.addAdmin(admin);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Admin added", response.getBody());
    }

    /**
     * Test case for the getAdminById method, which retrieves an administrator by ID.
     * The test checks that the method returns a ResponseEntity with the expected status and body.
     */
    @Test
    void testGetAdminById() {
        Administrateur admin = new Administrateur();
        when(administrateurService.getAdminById(anyLong())).thenReturn(ResponseEntity.of(Optional.of(admin)));

        ResponseEntity<Administrateur> response = administrateurController.getAdminById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    /**
     * Test case for the getAllAdmin method, which retrieves all administrators.
     * The test checks that the method returns a List of Administrateur objects with the expected size and content.
     */
    @Test
    void testGetAllAdmin() {
        Administrateur admin1 = new Administrateur();
        Administrateur admin2 = new Administrateur();
        List<Administrateur> adminList = Arrays.asList(admin1, admin2);
        when(administrateurService.getAllAdmin()).thenReturn(adminList);

        List<Administrateur> response = administrateurController.getAllAdmin();

        assertEquals(2, response.size());
        assertEquals(adminList, response);
    }

    /**
     * Test case for the deleteAdminById method, which deletes an administrator by ID.
     * The test checks that the method returns a ResponseEntity with the expected status and body.
     */
    @Test
    void testDeleteAdminById() {
        when(administrateurService.deleteAdminById(anyLong())).thenReturn(ResponseEntity.status(HttpStatus.OK).body("Admin deleted"));

        ResponseEntity<String> response = administrateurController.deleteAdminById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin deleted", response.getBody());
    }

    /**
     * Test case for the updateAddress method, which updates the address of an administrator.
     * The test checks that the method returns a ResponseEntity with the expected status and body.
     */
    @Test
    void testUpdateAddress() {
        when(administrateurService.updateAddress(anyLong(), anyString())).thenReturn(ResponseEntity.status(HttpStatus.OK).body("Address updated"));

        ResponseEntity<String> response = administrateurController.updateAddress(1L, "new address");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Address updated", response.getBody());
    }

    /**
     * Test case for the updatePhoneNumber method, which updates the phone number of an administrator.
     * The test checks that the method returns a ResponseEntity with the expected status and body.
     */
    @Test
    void testUpdatePhoneNumber() {
        when(administrateurService.updatePhoneNumber(anyLong(), anyLong())).thenReturn(ResponseEntity.status(HttpStatus.OK).body("Phone number updated"));

        ResponseEntity<String> response = administrateurController.updatePhoneNumber(1L, 1234567890L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Phone number updated", response.getBody());
    }

    /**
     * Test case for the updatePassword method, which updates the password of an administrator.
     * The test checks that the method returns a ResponseEntity with the expected status and body.
     */
    @Test
    void testUpdatePassword() {
        when(administrateurService.updatePassword(anyLong(), anyString())).thenReturn(ResponseEntity.status(HttpStatus.OK).body("Password updated"));

        ResponseEntity<String> response = administrateurController.updatePassword(1L, "new password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated", response.getBody());
    }

    /**
     * Test case for the addAdmin method with invalid data.
     * The test checks that the method returns a ResponseEntity with the expected status and body.
     */
    @Test
    void testAddAdminWithInvalidData() {
        Administrateur invalidAdmin = new Administrateur();
        when(administrateurService.addAdmin(any(Administrateur.class))).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data"));

        ResponseEntity<String> response = administrateurController.addAdmin(invalidAdmin);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody());
    }

    /**
     * Test case for the getAdminById method when the administrator is not found.
     * The test checks that the method returns a ResponseEntity with the expected status.
     */
    @Test
    void testGetAdminByIdNotFound() {
        when(administrateurService.getAdminById(anyLong())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        ResponseEntity<Administrateur> response = administrateurController.getAdminById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test case for the deleteAdminById method when the administrator is not found.
     * The test checks that the method returns a ResponseEntity with the expected status and body.
     */
    @Test
    void testDeleteAdminByIdNotFound() {
        when(administrateurService.deleteAdminById(anyLong())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found"));

        ResponseEntity<String> response = administrateurController.deleteAdminById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Admin not found", response.getBody());
    }
}