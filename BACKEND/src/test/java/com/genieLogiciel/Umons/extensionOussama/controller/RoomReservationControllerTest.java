package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.ReservationReason;
import com.genieLogiciel.Umons.extensionOussama.model.RoomReservation;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomReservationRepository;
import com.genieLogiciel.Umons.extensionOussama.service.RoomReservationService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for the RoomReservationController class.
 * This class contains unit tests for the various methods of the RoomReservationController.
 */
public class RoomReservationControllerTest {

    @InjectMocks
    private RoomReservationController roomReservationController;

    @Mock
    private RoomReservationService roomReservationService;

    @Mock
    private RoomReservationRepository roomReservationRepository;

    /**
     * Set up the necessary mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the addRoomReservationRequest method.
     * This method should add a new room reservation request and return a successful response.
     */
    @Test
    void testAddRoomReservationRequest() {
        RoomReservation reservation = new RoomReservation();
        when(roomReservationService.addRoomReservationRequest(any(RoomReservation.class)))
                .thenReturn(ResponseEntity.ok("Reservation added successfully"));

        ResponseEntity<String> response = roomReservationController.addRoomReservationRequest(reservation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reservation added successfully", response.getBody());
    }

    /**
     * Test the cancelReservation method.
     * This method should cancel a reservation and return a successful response.
     */
    @Test
    void testCancelReservation() {
        when(roomReservationService.cancelReservation(anyLong()))
                .thenReturn(ResponseEntity.ok("Reservation cancelled successfully"));

        ResponseEntity<String> response = roomReservationController.cancelReservation(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reservation cancelled successfully", response.getBody());
    }

    /**
     * Test the getReservationById method.
     * This method should return a reservation by its ID.
     */
    @Test
    void testGetReservationById() {
        RoomReservation reservation = new RoomReservation();
        when(roomReservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        ResponseEntity<RoomReservation> response = roomReservationController.getReservationById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservation, response.getBody());
    }

    /**
     * Test the getReservationById method when the reservation is not found.
     * This method should return a 404 Not Found response.
     */
    @Test
    void testGetReservationByIdNotFound() {
        when(roomReservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<RoomReservation> response = roomReservationController.getReservationById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test the getAllReservationRequest method.
     * This method should return a list of all reservation requests.
     */
    @Test
    void testGetAllReservationRequest() {
        RoomReservation reservation1 = new RoomReservation();
        RoomReservation reservation2 = new RoomReservation();
        List<RoomReservation> reservations = Arrays.asList(reservation1, reservation2);
        when(roomReservationService.getAllReservationRequests(anyInt(), anyInt())).thenReturn(reservations);

        ResponseEntity<List<RoomReservation>> response = roomReservationController.getAllReservationRequest(1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservations, response.getBody());
    }

    /**
     * Test the getStatus method.
     * This method should return the status of a reservation.
     */
    @Test
    void testGetStatus() {
        when(roomReservationService.getStatus(anyLong()))
                .thenReturn(ResponseEntity.ok(RoomReservation.ReservationStatus.APPROVED));

        ResponseEntity<RoomReservation.ReservationStatus> response = roomReservationController.getStatus(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(RoomReservation.ReservationStatus.APPROVED, response.getBody());
    }

    /**
     * Test the findReservationByApplicantId method.
     * This method should return a reservation by the applicant's ID.
     */
    @Test
    void testFindReservationByApplicantId() {
        RoomReservation reservation = new RoomReservation();
        when(roomReservationService.findReservationByApplicantId(anyLong()))
                .thenReturn(ResponseEntity.ok(reservation));

        ResponseEntity<RoomReservation> response = roomReservationController.findReservationByApplicantId(123L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservation, response.getBody());
    }

    /**
     * Test the getAllReservationReasons method.
     * This method should return a list of all available reservation reasons.
     */
    @Test
    void testGetAllReservationReasons() {
        ReservationReason reason1 = ReservationReason.PRESENTATION;
        ReservationReason reason2 = ReservationReason.MEETING;
        List<ReservationReason> reasons = Arrays.asList(reason1, reason2);
        when(roomReservationService.getAllReservationReasons()).thenReturn(reasons);

        ResponseEntity<List<ReservationReason>> response = roomReservationController.getAllReservationReasons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reasons, response.getBody());
    }

    /**
     * Test the updateRoomReservationStatus method.
     * This method should update the status of a room reservation.
     */
    @Test
    void testUpdateRoomReservationStatus() {
        when(roomReservationService.updateRoomReservationStatus(anyLong(), any(RoomReservation.ReservationStatus.class)))
                .thenReturn(ResponseEntity.ok("Status updated successfully"));

        ResponseEntity<String> response = roomReservationController.updateRoomReservationStatus(1L, RoomReservation.ReservationStatus.APPROVED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated successfully", response.getBody());
    }

    /**
     * Test the executeVerifyAndUpdateExpiredReservations method.
     * This method should verify and update any expired reservations.
     */
    @Test
    void testExecuteVerifyAndUpdateExpiredReservations() {
        doNothing().when(roomReservationService).verifyAndUpdateExpiredReservations();

        roomReservationController.executeVerifyAndUpdateExpiredReservations();

        verify(roomReservationService, times(1)).verifyAndUpdateExpiredReservations();
    }

    /**
     * Test the addRoomReservationRequest method when there is a conflict.
     * This method should return a 409 Conflict response.
     */
    @Test
    void testAddRoomReservationRequestConflict() {
        RoomReservation reservation = new RoomReservation();
        when(roomReservationService.addRoomReservationRequest(any(RoomReservation.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CONFLICT).body("Reservation conflict"));

        ResponseEntity<String> response = roomReservationController.addRoomReservationRequest(reservation);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Reservation conflict", response.getBody());
    }
}