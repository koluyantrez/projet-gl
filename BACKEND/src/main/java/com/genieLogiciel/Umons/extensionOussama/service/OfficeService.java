package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.extensionOussama.model.Office;
import com.genieLogiciel.Umons.extensionOussama.repository.BatimentRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.OfficeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The OfficeService class provides methods for managing office-related operations.
 * It allows for retrieving, assigning, unassigning, updating, and deleting office records.
 */
@Service
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final BatimentRepository batimentRepository;

    /**
     * Constructs an OfficeService instance with the specified repositories.
     *
     * @param officeRepository  The OfficeRepository instance used for CRUD operations on Office entities.
     * @param batimentRepository The BatimentRepository instance used for CRUD operations on Batiment entities.
     */
    public OfficeService(OfficeRepository officeRepository, BatimentRepository batimentRepository) {
        this.officeRepository = officeRepository;
        this.batimentRepository = batimentRepository;
    }

    /**
     * Retrieves all offices from the repository.
     *
     * @return A list of all Office entities.
     */
    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    /**
     * Retrieves an office by its name.
     *
     * @param name The name of the office to retrieve.
     * @return An Optional containing the Office if found, or an empty Optional if not found.
     */
    public Optional<Office> getOfficeByName(String name) {
        return officeRepository.findByName(name);
    }

    /**
     * Assigns an office to a personnel member.
     * Marks the office as unavailable and sets the associated personnel name.
     *
     * @param officeName      The name of the office to be assigned.
     * @param personnelName   The name of the personnel to assign to the office.
     * @param startDate       The start date of the assignment.
     * @param endDate         The end date of the assignment.
     * @return The updated Office entity after assignment.
     * @throws IllegalStateException If the office is already assigned to someone else.
     */
    public Office assignOfficeToPersonnel(String officeName, String personnelName, LocalDate startDate, LocalDate endDate) {
        Optional<Office> officeOptional = officeRepository.findByName(officeName);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            if (office.getAssociatedPersonnel() != null && !office.getAssociatedPersonnel().isEmpty()) {
                throw new IllegalStateException("The office is already assigned to another personnel.");
            }

            office.setAssociatedPersonnel(personnelName);
            office.setAvailable(false); // Mark the office as unavailable

            // Save assignment changes
            // logOfficeAssignmentChange(officeName, personnelName, startDate, endDate, "ASSIGN");

            // Send notification
            // notificationService.sendOfficeAssignmentNotification(personnelName, officeName, startDate, endDate);

            return officeRepository.save(office);
        }
        return null;
    }

    /**
     * Unassigns an office from its current personnel.
     * Marks the office as available and removes the personnel association.
     *
     * @param officeName The name of the office to be unassigned.
     * @return The updated Office entity after unassignment.
     * @throws IllegalStateException If the office is not currently assigned to anyone.
     */
    public Office unassignOfficeFromPersonnel(String officeName) {
        Optional<Office> officeOptional = officeRepository.findByName(officeName);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            String previousOccupant = office.getAssociatedPersonnel();

            if (previousOccupant == null || previousOccupant.isEmpty()) {
                throw new IllegalStateException("The office is not currently assigned to any personnel.");
            }

            office.setAssociatedPersonnel(null);
            office.setAvailable(true); // Mark the office as available

            // Save unassignment changes
            // logOfficeAssignmentChange(officeName, previousOccupant, null, null, "UNASSIGN");

            // Send notification
            // notificationService.sendOfficeUnassignmentNotification(previousOccupant, officeName);

            return officeRepository.save(office);
        }
        return null;
    }

    /**
     * Retrieves all offices associated with a specific building.
     *
     * @param buildingName The name of the building to find offices in.
     * @return A list of Office entities associated with the specified building.
     */
    public List<Office> getOfficesByBuilding(String buildingName) {
        return officeRepository.findByBatiment(buildingName);
    }

    /**
     * Adds a new office to the repository and updates the associated building's records.
     *
     * @param newOffice The Office entity to be added.
     * @return A ResponseEntity containing the result of the operation.
     *         If successful, the status code is CREATED (201).
     *         If the building is not found, the status code is NOT_FOUND (404).
     */
    public ResponseEntity<String> addOffice(Office newOffice) {
        Optional<Batiment> batimentOptional = batimentRepository.findByName(newOffice.getBatiment());
        if (batimentOptional.isPresent()){
            Batiment batiment = batimentOptional.get();
            officeRepository.save(newOffice);
            newOffice.setName(newOffice.getBatiment() + "-" + newOffice.getId());
            Office savedOffice = officeRepository.save(newOffice);
            batiment.getAllOffices().add(newOffice.getName());
            batiment.setNumberOfRooms(batiment.getNumberOfRooms() + 1);
            batimentRepository.save(batiment);
            return ResponseEntity.status(HttpStatus.CREATED).body("Office " + savedOffice.getName() + " added successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Building not found");
    }

    /**
     * Updates an existing office with new details.
     *
     * @param name          The name of the office to be updated.
     * @param updatedOffice An Office entity containing the new details.
     * @return The updated Office entity, or null if the office was not found.
     */
    public Office updateOffice(String name, Office updatedOffice) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            office.setName(updatedOffice.getName());
            office.setAssociatedPersonnel(updatedOffice.getAssociatedPersonnel());
            office.setLocation(updatedOffice.getLocation());
            office.setAvailable(updatedOffice.getAvailable());
            office.setEquipment(updatedOffice.getEquipment());
            office.setBatiment(updatedOffice.getBatiment());
            return officeRepository.save(office);
        }
        return null;
    }

    /**
     * Deletes an office by its name.
     *
     * @param name The name of the office to be deleted.
     */
    public void deleteOffice(String name) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        officeOptional.ifPresent(office -> officeRepository.deleteById(office.getId()));
    }

    /**
     * Updates the occupant of an office, with an option to mark the office for deletion.
     *
     * @param name                The name of the office to be updated.
     * @param newOccupant         The name of the new occupant to assign.
     * @param pendingConfirmation Indicates if the update should be marked as pending confirmation.
     * @return An Optional containing the updated Office if found, or an empty Optional if not found.
     */
    public Optional<Office> updateOccupant(String name, String newOccupant, Boolean pendingConfirmation) {
        Optional<Office> optionalOffice = officeRepository.findByName(name);
        if (!optionalOffice.isPresent()) {
            return Optional.empty();
        }

        Office office = optionalOffice.get();
        if (pendingConfirmation != null && pendingConfirmation) {
            office.setDeletionPending(true);
        } else {
            office.setAssociatedPersonnel(newOccupant);
            office.setDeletionPending(false);
        }

        officeRepository.save(office);
        return Optional.of(office);
    }

    /**
     * Marks an office for deletion.
     *
     * @param name The name of the office to be marked for deletion.
     * @return The updated Office entity marked for deletion, or null if not found.
     */
    public Office markOfficeForDeletion(String name) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            office.setDeletionPending(true); // Mark the office as pending deletion
            officeRepository.save(office);
            // You may send a notification here if needed
            return office;
        }
        return null;
    }

    /**
     * Confirms the deletion of an office and updates the associated building's records.
     *
     * @param name The name of the office to be confirmed for deletion.
     */
    public void confirmOfficeDeletion(String name) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            // Retrieve the building
            Optional<Batiment> batimentOptional = batimentRepository.findByName(office.getBatiment());
            if (batimentOptional.isPresent()) {
                Batiment batiment = batimentOptional.get();
                batiment.getAllOffices().remove(office.getName());
                batiment.setNumbersOfOffices(batiment.getNumbersOfOffices() - 1);
                batimentRepository.save(batiment);
            }
            officeRepository.deleteById(office.getId());
        }
    }

    /**
     * Updates the equipment list for a specific office.
     *
     * @param name         The name of the office to be updated.
     * @param newEquipment The new list of equipment to be set.
     * @return The updated Office entity with the new equipment list, or null if not found.
     */
    public Office updateEquipment(String name, List<String> newEquipment) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            office.setEquipment(newEquipment);
            return officeRepository.save(office);
        }
        return null;
    }

    /**
     * Updates the availability status of an office.
     *
     * @param name         The name of the office to be updated.
     * @param availability The new availability status to be set.
     * @return The updated Office entity with the new availability status, or null if not found.
     */
    public Office updateAvailability(String name, Boolean availability) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            office.setAvailable(availability);
            return officeRepository.save(office);
        }
        return null;
    }
}
