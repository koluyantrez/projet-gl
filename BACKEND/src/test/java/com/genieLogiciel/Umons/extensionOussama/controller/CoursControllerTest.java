package com.genieLogiciel.Umons.extensionOussama.controller;

import com.genieLogiciel.Umons.extensionOussama.model.Cours;
import com.genieLogiciel.Umons.extensionOussama.service.CoursService;
import com.genieLogiciel.Umons.repository.ProfesseurRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for the CoursController.
 * This class contains unit tests for the various methods of the CoursController class.
 */
public class CoursControllerTest {

    @InjectMocks
    private CoursController coursController;

    @Mock
    private CoursService coursService;

    @Mock
    private ProfesseurRepository professeurRepository;

    /**
     * Sets up the test environment by initializing the mock objects.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for adding a new course.
     * Verifies that the addNewCours method of the CoursController returns the expected response.
     */
    @Test
    void testAddNewCours() {
        Cours cours = new Cours();
        when(coursService.addNewCours(any(Cours.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Cours added"));

        ResponseEntity<String> response = coursController.addNewCours(cours);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Cours added", response.getBody());
    }

    /**
     * Test case for deleting a course by ID.
     * Verifies that the deleteCoursById method of the CoursController returns the expected response.
     */
    @Test
    void testDeleteCoursById() {
        when(coursService.deleteCoursById(anyLong())).thenReturn(ResponseEntity.status(HttpStatus.OK).body("Cours deleted"));

        ResponseEntity<String> response = coursController.deleteCoursById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cours deleted", response.getBody());
    }

    /**
     * Test case for getting a course by name.
     * Verifies that the getCoursByName method of the CoursController returns the expected response.
     */
    @Test
    void testGetCoursByName() {
        Cours cours = new Cours();
        when(coursService.getCoursByName(anyString())).thenReturn(ResponseEntity.of(Optional.of(cours)));

        ResponseEntity<Cours> response = coursController.getCoursByName("Cours1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cours, response.getBody());
    }

    /**
     * Test case for getting a course by name when the course is not found.
     * Verifies that the getCoursByName method of the CoursController returns the expected response.
     */
    @Test
    void testGetCoursByNameNotFound() {
        when(coursService.getCoursByName(anyString())).thenReturn(ResponseEntity.of(Optional.empty()));

        ResponseEntity<Cours> response = coursController.getCoursByName("Cours1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test case for deleting a course by name.
     * Verifies that the deleteCoursByName method of the CoursController returns the expected response.
     */
    @Test
    void testDeleteCoursByName() {
        when(coursService.deleteCoursByName(anyString())).thenReturn(ResponseEntity.status(HttpStatus.OK).body("Cours deleted"));

        ResponseEntity<String> response = coursController.deleteCoursByName("Cours1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cours deleted", response.getBody());
    }

    /**
     * Test case for deleting all courses.
     * Verifies that the deleteAllCours method of the CoursController returns the expected response.
     */
    @Test
    void testDeleteAllCours() {
        when(coursService.deleteAllCours()).thenReturn(ResponseEntity.status(HttpStatus.OK).body("All cours deleted"));

        ResponseEntity<String> response = coursController.deleteAllCours();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("All cours deleted", response.getBody());
    }

    /**
     * Test case for getting the list of students for a specific course.
     * Verifies that the studentListOfThisCours method of the CoursController returns the expected response.
     */
    @Test
    void testStudentListOfThisCours() {
        List<String> students = Arrays.asList("Student1", "Student2");
        when(coursService.studentListForThisCours(anyString())).thenReturn(ResponseEntity.ok(students));

        ResponseEntity<List<String>> response = coursController.studentListOfThisCours("Cours1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, response.getBody());
    }

    /**
     * Test case for getting the list of all teachers for a specific course.
     * Verifies that the listOfAllTeacherForThisCours method of the CoursController returns the expected response.
     */
    @Test
    void testListOfAllTeacherForThisCours() {
        List<String> teachers = Arrays.asList("Teacher1", "Teacher2");
        when(coursService.listOfAllTeachersToThisCours(anyString())).thenReturn(ResponseEntity.ok(teachers));

        ResponseEntity<List<String>> response = coursController.listOfAllTeacherForThisCours("Cours1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teachers, response.getBody());
    }

    /**
     * Test case for getting the list of all courses.
     * Verifies that the getAllCours method of the CoursController returns the expected response.
     */
    @Test
    void testGetAllCours() {
        Cours cours1 = new Cours();
        Cours cours2 = new Cours();
        List<Cours> coursList = Arrays.asList(cours1, cours2);
        when(coursService.getAllCours()).thenReturn(ResponseEntity.ok(coursList));

        ResponseEntity<List<Cours>> response = coursController.getAllCours();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coursList, response.getBody());
    }

    /**
     * Test case for deleting a course by name when the course is not found.
     * Verifies that the deleteCoursByName method of the CoursController returns the expected response.
     */
    @Test
    void testDeleteCoursByNameNotFound() {
        when(coursService.deleteCoursByName(anyString())).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cours not found"));

        ResponseEntity<String> response = coursController.deleteCoursByName("Cours1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cours not found", response.getBody());
    }
}