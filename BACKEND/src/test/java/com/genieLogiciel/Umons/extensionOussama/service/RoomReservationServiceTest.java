package com.genieLogiciel.Umons.extensionOussama.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.model.RoomReservation;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomReservationRepository;
import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.model.User;
import com.genieLogiciel.Umons.repository.StudentRepository;
import com.genieLogiciel.Umons.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Unit tests for the RoomReservationService class.
 * This class tests various scenarios for managing room reservations, including adding, cancelling, and querying reservations.
 */
@SpringBootTest
public class RoomReservationServiceTest {

    @Mock
    private RoomReservationRepository roomReservationRepository; // Mocked repository for RoomReservation entities.

    @Mock
    private StudentRepository studentRepository; // Mocked repository for Student entities.

    @Mock
    private RoomRepository roomRepository; // Mocked repository for Room entities.

    @Mock
    private UserRepository userRepository; // Mocked repository for User entities.

    @InjectMocks
    private RoomReservationService roomReservationService; // The service being tested.

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test.
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for adding a room reservation request when the user is not found.
     * Verifies that the service responds with NOT_FOUND and an appropriate message if the user does not exist.
     */
    @Test
    void testAddRoomReservationRequest_UserNotFound() {
        RoomReservation reservationRequest = new RoomReservation();
        reservationRequest.setFirstName("oussama");
        reservationRequest.setLastName("hakik");

        when(userRepository.findFirstByName("oussama hakik")).thenReturn(Optional.empty());

        ResponseEntity<String> response = roomReservationService.addRoomReservationRequest(reservationRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"User not found\"}", response.getBody());
    }

    /**
     * Test case for adding a room reservation request when the reservation date is in the past.
     * Verifies that the service responds with BAD_REQUEST and an appropriate message if the reservation date is before today.
     */
    @Test
    void testAddRoomReservationRequest_ReservationDateInPast() {
        RoomReservation reservationRequest = new RoomReservation();
        reservationRequest.setFirstName("oussama");
        reservationRequest.setLastName("hakik");
        reservationRequest.setDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(userRepository.findFirstByName("oussama hakik")).thenReturn(Optional.of(new User()));

        ResponseEntity<String> response = roomReservationService.addRoomReservationRequest(reservationRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Reservation date cannot be in the past\"}", response.getBody());
    }

    /**
     * Test case for adding a room reservation request with an invalid reservation time.
     * Verifies that the service responds with BAD_REQUEST and an appropriate message if the reservation start time is not within the allowed range.
     */
    @Test
    void testAddRoomReservationRequest_InvalidReservationTime() {
        RoomReservation reservationRequest = new RoomReservation();
        reservationRequest.setFirstName("oussama");
        reservationRequest.setLastName("hakik");
        reservationRequest.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservationRequest.setStart("07:00"); // Time before allowed range.

        when(userRepository.findFirstByName("oussama hakik")).thenReturn(Optional.of(new User()));

        ResponseEntity<String> response = roomReservationService.addRoomReservationRequest(reservationRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Reservation time must be between 8 AM and 11 PM\"}", response.getBody());
    }

    /**
     * Test case for adding a room reservation request when the desired room is not found.
     * Verifies that the service responds with NOT_FOUND and an appropriate message if the specified room does not exist.
     */
    @Test
    void testAddRoomReservationRequest_RoomNotFound() {
        RoomReservation reservationRequest = new RoomReservation();
        reservationRequest.setFirstName("oussama");
        reservationRequest.setLastName("hakik");
        reservationRequest.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservationRequest.setStart("10:00");
        reservationRequest.setDesiredRoom("Conference Room");

        when(userRepository.findFirstByName("oussama hakik")).thenReturn(Optional.of(new User()));
        when(roomRepository.getRoomByName("Conference Room")).thenReturn(Optional.empty());

        ResponseEntity<String> response = roomReservationService.addRoomReservationRequest(reservationRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("{\"message\":\"Room not found\"}", response.getBody());
    }

    /**
     * Test case for successfully adding a room reservation request.
     * Verifies that the service responds with OK and a success message when all conditions are met.
     */
    @Test
    void testAddRoomReservationRequest_Success() {
        RoomReservation reservationRequest = new RoomReservation();
        reservationRequest.setFirstName("oussama");
        reservationRequest.setLastName("hakik");
        reservationRequest.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        reservationRequest.setStart("10:00");
        reservationRequest.setDesiredRoom("Conference Room");

        when(userRepository.findFirstByName("oussama hakik")).thenReturn(Optional.of(new User()));
        when(roomRepository.getRoomByName("Conference Room")).thenReturn(Optional.of(new Room()));

        ResponseEntity<String> response = roomReservationService.addRoomReservationRequest(reservationRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Reservation request submitted successfully\"}", response.getBody());
    }

    /**
     * Test case for cancelling a room reservation when the reservation is not found.
     * Verifies that the service responds with NOT_FOUND and an appropriate message if the reservation does not exist.
     */
    @Test
    void testCancelReservation_ReservationNotFound() {
        when(roomReservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<String> response = roomReservationService.cancelReservation(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Reservation not found", response.getBody());
    }

    /**
     * Test case for successfully cancelling a room reservation.
     * Verifies that the service responds with OK and a success message when the reservation exists and is successfully deleted.
     */
    @Test
    void testCancelReservation_Success() {
        when(roomReservationRepository.findById(anyLong())).thenReturn(Optional.of(new RoomReservation()));

        ResponseEntity<String> response = roomReservationService.cancelReservation(1L);
        verify(roomReservationRepository).deleteById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Delete success", response.getBody());
    }

    /**
     * Test case for retrieving all room reservation requests with pagination.
     * Verifies that the service returns a paginated list of reservations and the page size is correctly applied.
     */
    @Test
    void testGetAllReservationRequests() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoomReservation> page = new PageImpl<>(List.of(new RoomReservation()), pageable, 1);
        when(roomReservationRepository.findAll(pageable)).thenReturn(page);

        List<RoomReservation> reservations = roomReservationService.getAllReservationRequests(1, 10);
        assertEquals(1, reservations.size());
    }

    /**
     * Test case for retrieving the status of a room reservation when the reservation is not found.
     * Verifies that the service responds with NOT_FOUND and no body if the reservation does not exist.
     */
    @Test
    void testGetStatus_ReservationNotFound() {
        when(roomReservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<RoomReservation.ReservationStatus> response = roomReservationService.getStatus(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Test case for retrieving the status of a room reservation successfully.
     * Verifies that the service returns the correct status when the reservation exists.
     */
    @Test
    void testGetStatus_Success() {
        RoomReservation reservation = new RoomReservation();
        reservation.setReservationStatus(RoomReservation.ReservationStatus.PENDING);
        when(roomReservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        ResponseEntity<RoomReservation.ReservationStatus> response = roomReservationService.getStatus(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(RoomReservation.ReservationStatus.PENDING, response.getBody());
    }

    /**
     * Test case for finding a reservation by applicant ID when the student is not found.
     * Verifies that the service responds with NOT_FOUND and no body if the student does not exist.
     */
    @Test
    void testFindReservationByApplicantId_StudentNotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<RoomReservation> response = roomReservationService.findReservationByApplicantId(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Add more tests for `findReservationByApplicantId` if you implement the logic.

    /**
     * Test case for verifying and updating expired reservations.
     * Verifies that the service updates the reservation status to EXPIRED for reservations that are past their date.
     */
    @Test
    void testVerifyAndUpdateExpiredReservations() {
        RoomReservation reservation = new RoomReservation();
        reservation.setReservationStatus(RoomReservation.ReservationStatus.PENDING);
        reservation.setDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(roomReservationRepository.findByReservationStatusAndDateBefore(any(), any())).thenReturn(List.of(reservation));

        roomReservationService.verifyAndUpdateExpiredReservations();
        verify(roomReservationRepository).save(reservation);
        assertEquals(RoomReservation.ReservationStatus.EXPIRED, reservation.getReservationStatus());
    }

    /**
     * Test case for updating the status of a room reservation when the reservation is not found.
     * Verifies that the service responds with NOT_FOUND and an appropriate message if the reservation does not exist.
     */
    @Test
    void testUpdateRoomReservationStatus_ReservationNotFound() {
        when(roomReservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<String> response = roomReservationService.updateRoomReservationStatus(1L, RoomReservation.ReservationStatus.APPROVED);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Reservation not found", response.getBody());
    }

    /**
     * Test case for successfully updating the status of a room reservation.
     * Verifies that the service updates the reservation status and responds with OK and a success message.
     */
    @Test
    void testUpdateRoomReservationStatus_Success() {
        RoomReservation reservation = new RoomReservation();
        when(roomReservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        ResponseEntity<String> response = roomReservationService.updateRoomReservationStatus(1L, RoomReservation.ReservationStatus.APPROVED);
        verify(roomReservationRepository).save(reservation);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated successfully", response.getBody());
    }
}
