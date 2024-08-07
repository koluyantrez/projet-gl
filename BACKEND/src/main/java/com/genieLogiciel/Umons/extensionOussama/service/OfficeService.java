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

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final BatimentRepository batimentRepository;

    public OfficeService(OfficeRepository officeRepository, BatimentRepository batimentRepository) {
        this.officeRepository = officeRepository;
        this.batimentRepository = batimentRepository;
    }

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    public Optional<Office> getOfficeByName(String name) {
        return officeRepository.findByName(name);
    }


    public Office assignOfficeToPersonnel(String officeName, String personnelName, LocalDate startDate, LocalDate endDate) {
        Optional<Office> officeOptional = officeRepository.findByName(officeName);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            if (office.getAssociatedPersonnel() != null && !office.getAssociatedPersonnel().isEmpty()) {
                throw new IllegalStateException("Le bureau est déjà attribué à un autre personnel.");
            }

            office.setAssociatedPersonnel(personnelName);
            office.setAvailable(false); // Marquer le bureau comme non disponible

            // Enregistrez les changements d'attribution
            //logOfficeAssignmentChange(officeName, personnelName, startDate, endDate, "ASSIGN");

            // Envoyer une notification
            //notificationService.sendOfficeAssignmentNotification(personnelName, officeName, startDate, endDate);

            return officeRepository.save(office);
        }
        return null;
    }

    public Office unassignOfficeFromPersonnel(String officeName) {
        Optional<Office> officeOptional = officeRepository.findByName(officeName);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            String previousOccupant = office.getAssociatedPersonnel();

            if (previousOccupant == null || previousOccupant.isEmpty()) {
                throw new IllegalStateException("Le bureau n'est pas actuellement attribué à un personnel.");
            }

            office.setAssociatedPersonnel(null);
            office.setAvailable(true); // Marquer le bureau comme disponible

            // Enregistrez les changements de désattribution
            //logOfficeAssignmentChange(officeName, previousOccupant, null, null, "UNASSIGN");

            // Envoyer une notification
            //.sendOfficeUnassignmentNotification(previousOccupant, officeName);

            return officeRepository.save(office);
        }
        return null;
    }

    public List<Office> getOfficesByBuilding(String buildingName) {
        return officeRepository.findByBatiment(buildingName);
    }


    public ResponseEntity<String> addOffice(Office newOffice) {
        Optional<Batiment> batimentOptional = batimentRepository.findByName(newOffice.getBatiment());
        if (batimentOptional.isPresent()){
            Batiment batiment = batimentOptional.get();
            officeRepository.save(newOffice);
            newOffice.setName(newOffice.getBatiment()+"-"+newOffice.getId());
            Office savedOffice = officeRepository.save(newOffice);
            batiment.getAllOffices().add(newOffice.getName());
            batiment.setNumberOfRooms(batiment.getNumberOfRooms()+1);
            batimentRepository.save(batiment);
            return ResponseEntity.status(HttpStatus.CREATED).body("Office " + savedOffice.getName() + " added successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Building not found");
    }

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

    public void deleteOffice(String name) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        officeOptional.ifPresent(office -> officeRepository.deleteById(office.getId()));
    }

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

    public Office markOfficeForDeletion(String name) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            office.setDeletionPending(true); // Marquer le bureau comme en attente
            officeRepository.save(office);
            // Vous pouvez envoyer une notification ici si nécessaire
            return office;
        }
        return null;
    }

    public void confirmOfficeDeletion(String name) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            // Récupérer le bâtiment
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

    public Office updateEquipment(String name, List<String> newEquipment) {
        Optional<Office> officeOptional = officeRepository.findByName(name);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();
            office.setEquipment(newEquipment);
            return officeRepository.save(office);
        }
        return null;
    }

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
