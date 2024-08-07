package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.extensionOussama.model.Office;
import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.model.RoomStatus;
import com.genieLogiciel.Umons.extensionOussama.repository.BatimentRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.OfficeRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The BatimentService class provides methods to manage buildings, rooms, and offices.
 * It interacts with the BatimentRepository, RoomRepository, and OfficeRepository to perform CRUD operations.
 */
@Service
public class BatimentService {
    private final BatimentRepository batimentRepository;
    private final RoomRepository roomRepository;
    private final OfficeRepository officeRepository;

    /**
     * Constructs a BatimentService instance with the provided repositories.
     *
     * @param batimentRepository The BatimentRepository instance.
     * @param roomRepository     The RoomRepository instance.
     * @param officeRepository   The OfficeRepository instance.
     */
    @Autowired
    public BatimentService(BatimentRepository batimentRepository, RoomRepository roomRepository, OfficeRepository officeRepository) {
        this.batimentRepository = batimentRepository;
        this.roomRepository = roomRepository;
        this.officeRepository = officeRepository;
    }

    /**
     * Removes a room from a building.
     *
     * @param roomName     The name of the room to be removed.
     * @param buildingName The name of the building to remove the room from.
     * @return An Optional String indicating the result of the operation ("Remove Success", "Room not Found", or "Building not Found").
     */
    public Optional<String> removeRoomFromBuilding(String roomName, String buildingName) {
        Optional<Room> roomOptional = roomRepository.getRoomByName(roomName);
        Optional<Batiment> batimentOptional = batimentRepository.findByName(buildingName);

        if (roomOptional.isPresent() && batimentOptional.isPresent()) {
            Room room = roomOptional.get();
            Batiment batiment = batimentOptional.get();

            room.setAvailable(false);
            room.setAssociatedBuilding(null);
            batiment.setNumberOfRooms(batiment.getNumberOfRooms() - 1);
            batiment.getAllRooms().remove(roomName);

            roomRepository.save(room);
            batimentRepository.save(batiment);

            return Optional.of("Remove Success");
        }

        if (roomOptional.isEmpty()) {
            return Optional.of("Room not Found");
        }

        return Optional.of("Building not Found");
    }

    /**
     * Adds an office to a building.
     *
     * @param officeName The name of the office to be added.
     * @return An Optional String indicating the result of the operation ("Office added to building successfully", "Office Not Found", or "Building Not Found").
     */
    public Optional<String> addOfficeToBuilding(String officeName) {
        if (officeName == null || officeName.isEmpty()) {
            return Optional.of("Office Name cannot be null or empty");
        }
        Optional<Office> officeOptional = officeRepository.findByName(officeName);
        if (officeOptional.isPresent()) {
            Office office = officeOptional.get();

            Optional<Batiment> batimentOptional = batimentRepository.findByName(office.getBatiment());
            if (batimentOptional.isPresent()) {
                Batiment batiment = batimentOptional.get();
                batiment.getAllOffices().add(office.getName());
                batiment.setNumbersOfOffices(batiment.getNumberOfRooms() + 1);
                batimentRepository.save(batiment);
                return Optional.of("Office added to building successfully");
            } else {
                return Optional.of("Building Not Found");
            }
        } else {
            return Optional.of("Office Not Found");
        }
    }

    /**
     * Removes an office from a building.
     *
     * @param officeName   The name of the office to be removed.
     * @param buildingName The name of the building to remove the office from.
     * @return An Optional String indicating the result of the operation ("Office removed from building successfully", "Office not Found", or "Building not Found").
     */
    @Transactional
    public Optional<String> removeOfficeFromBuilding(String officeName, String buildingName) {
        Optional<Office> officeOptional = officeRepository.findByName(officeName);
        Optional<Batiment> batimentOptional = batimentRepository.findByName(buildingName);

        if (officeOptional.isPresent() && batimentOptional.isPresent()) {
            Office office = officeOptional.get();
            Batiment batiment = batimentOptional.get();

            // Remove the office from the building
            batiment.getAllOffices().remove(office.getName());
            batiment.setNumbersOfOffices(batiment.getNumbersOfOffices() - 1);
            office.setBatiment(null); // Dissociate the office from the building
            office.setAssociatedPersonnel(null);
            office.setLocation(null);
            officeRepository.save(office);
            return Optional.of("Office removed from building successfully");
        }

        if (officeOptional.isEmpty()) {
            return Optional.of("Office not Found");
        }

        return Optional.of("Building not Found");
    }

    /**
     * Adds a room to a building.
     *
     * @param roomName The name of the room to be added.
     * @return An Optional String indicating the result of the operation ("Room added to building successfully", "Room Not Found", or "Building Not Found").
     */
    public Optional<String> addRoomToBuild(String roomName) {
        if (roomName == null || roomName.isEmpty()) {
            return Optional.of("Room Name cannot be null or empty");
        }

        Optional<Room> roomOptional = roomRepository.getRoomByName(roomName);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();

            Optional<Batiment> batimentOptional = batimentRepository.findByName(room.getAssociatedBuilding());
            if (batimentOptional.isPresent()) {
                Batiment batiment = batimentOptional.get();
                if (!Objects.equals(room.getType(), "OFFICE")) {
                    batiment.getAllRooms().add(room.getName());
                    batiment.setNumberOfRooms(batiment.getNumberOfRooms() + 1);
                }
                batimentRepository.save(batiment);

                return Optional.of("Room added to building successfully");
            } else {
                return Optional.of("Building Not Found");
            }
        } else {
            return Optional.of("Room Not Found");
        }
    }

    /**
     * Adds a new building.
     *
     * @param newBuilding The new building to be added.
     * @return The saved Batiment object.
     */
    public Batiment addBuilding(Batiment newBuilding) {
        return batimentRepository.save(newBuilding);
    }

    /**
     * Retrieves a building by its ID.
     *
     * @param id The ID of the building to be retrieved.
     * @return The Batiment object.
     * @throws ChangeSetPersister.NotFoundException If the building is not found.
     */
    public Batiment getBuildingById(Long id) throws ChangeSetPersister.NotFoundException {
        return batimentRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    /**
     * Deletes a building by its ID.
     *
     * @param id The ID of the building to be deleted.
     */
    public void deleteBuildingById(Long id) {
        batimentRepository.deleteById(id);
    }

    /**
     * Updates the availability of a building.
     *
     * @param buildingName  The name of the building.
     * @param availability  The new availability status of the building.
     * @return The updated Batiment object.
     */
    public Batiment updateBuildingAvailability(String buildingName, Boolean availability) {
        Optional<Batiment> batimentOptional = batimentRepository.findByName(buildingName);
        if (batimentOptional.isPresent()) {
            Batiment batiment = batimentOptional.get();
            batiment.setAvailable(availability);
            if (!availability) {
                List<Room> rooms = roomRepository.findByAssociatedBuilding(batiment.getName());
                for (Room room : rooms) {
                    room.setAvailable(false);
                    room.setStatus(RoomStatus.UNAVAILABLE);
                    roomRepository.save(room);
                }
            }
            return batimentRepository.save(batiment);
        }
        return null;
    }

    /**
     * Retrieves all buildings.
     *
     * @return A list of all Batiment objects.
     */
    public List<Batiment> getAllBuildings() {
        return batimentRepository.findAll();
    }

    /**
     * Retrieves a building by its name.
     *
     * @param name The name of the building.
     * @return An Optional Batiment object.
     */
    public Optional<Batiment> getBuildingByName(String name) {
        return batimentRepository.findByName(name);
    }

    /**
     * Deletes a building by its name.
     *
     * @param name The name of the building to be deleted.
     * @return True if the building was deleted, false otherwise.
     */
    public boolean deleteBuildingByName(String name) {
        Optional<Batiment> batimentOptional = batimentRepository.findByName(name);
        if (batimentOptional.isPresent()) {
            Batiment batiment = batimentOptional.get();
            List<Room> rooms = roomRepository.findByAssociatedBuilding(batiment.getName());
            for (Room room : rooms) {
                room.setAssociatedBuilding(null);
            }
            batimentRepository.deleteByName(name);
            return true;
        }
        return false;
    }
}