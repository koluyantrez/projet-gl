package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.extensionOussama.model.Office;
import com.genieLogiciel.Umons.extensionOussama.repository.BatimentRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.OfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the OfficeService class.
 * This class verifies the behavior of the OfficeService methods by mocking the OfficeRepository and BatimentRepository dependencies.
 */
class OfficeServiceTest {

    @Mock
    private OfficeRepository officeRepository; // Mocked repository used by the OfficeService.

    @Mock
    private BatimentRepository batimentRepository; // Mocked repository for building data, though not used in these tests.

    @InjectMocks
    private OfficeService officeService; // The service being tested.

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test.
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for retrieving all offices.
     * Verifies that the OfficeService returns the correct list of offices when the OfficeRepository provides the offices.
     */
    @Test
    void testGetAllOffices() {
        List<Office> offices = new ArrayList<>();
        offices.add(new Office());
        offices.add(new Office());

        when(officeRepository.findAll()).thenReturn(offices);

        List<Office> result = officeService.getAllOffices();

        assertEquals(2, result.size());
        verify(officeRepository, times(1)).findAll(); // Verify that findAll was called once.
    }

    /**
     * Test case for retrieving an office by its name.
     * Verifies that the OfficeService returns the correct office when it exists in the repository.
     */
    @Test
    void testGetOfficeByName() {
        Office office = new Office();
        office.setName("Office 1");

        when(officeRepository.findByName("Office 1")).thenReturn(Optional.of(office));

        Optional<Office> result = officeService.getOfficeByName("Office 1");

        assertTrue(result.isPresent()); // Verify that the office is present.
        assertEquals("Office 1", result.get().getName()); // Verify the office name.
        verify(officeRepository, times(1)).findByName("Office 1"); // Verify that findByName was called once.
    }

    /**
     * Test case for assigning an office to a personnel.
     * Verifies that the OfficeService updates the office availability and associated personnel correctly.
     */
    @Test
    void testAssignOfficeToPersonnel() {
        Office office = new Office();
        office.setName("Office 1");
        office.setAvailable(true);

        when(officeRepository.findByName("Office 1")).thenReturn(Optional.of(office));
        when(officeRepository.save(office)).thenReturn(office);

        Office result = officeService.assignOfficeToPersonnel("Office 1", "John Doe", LocalDate.now(), LocalDate.now().plusDays(30));

        assertNotNull(result); // Verify that the result is not null.
        assertFalse(result.getAvailable()); // Verify that the office is not available.
        assertEquals("John Doe", result.getAssociatedPersonnel()); // Verify the associated personnel.
        verify(officeRepository, times(1)).findByName("Office 1"); // Verify that findByName was called once.
        verify(officeRepository, times(1)).save(office); // Verify that save was called once.
    }

    /**
     * Test case for unassigning personnel from an office.
     * Verifies that the OfficeService updates the office availability and clears associated personnel correctly.
     */
    @Test
    void testUnassignOfficeFromPersonnel() {
        Office office = new Office();
        office.setName("Office 1");
        office.setAssociatedPersonnel("John Doe");
        office.setAvailable(false);

        when(officeRepository.findByName("Office 1")).thenReturn(Optional.of(office));
        when(officeRepository.save(office)).thenReturn(office);

        Office result = officeService.unassignOfficeFromPersonnel("Office 1");

        assertNotNull(result); // Verify that the result is not null.
        assertTrue(result.getAvailable()); // Verify that the office is available.
        assertNull(result.getAssociatedPersonnel()); // Verify that the associated personnel is null.
        verify(officeRepository, times(1)).findByName("Office 1"); // Verify that findByName was called once.
        verify(officeRepository, times(1)).save(office); // Verify that save was called once.
    }

    /**
     * Test case for retrieving offices by building.
     * Verifies that the OfficeService returns the correct list of offices for a given building.
     */
    @Test
    void testGetOfficesByBuilding() {
        List<Office> offices = new ArrayList<>();
        offices.add(new Office());
        offices.add(new Office());

        when(officeRepository.findByBatiment("Building 1")).thenReturn(offices);

        List<Office> result = officeService.getOfficesByBuilding("Building 1");

        assertEquals(2, result.size()); // Verify the number of offices returned.
        verify(officeRepository, times(1)).findByBatiment("Building 1"); // Verify that findByBatiment was called once.
    }

}
