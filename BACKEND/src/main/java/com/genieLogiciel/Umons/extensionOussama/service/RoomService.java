package com.genieLogiciel.Umons.extensionOussama.service;

import com.genieLogiciel.Umons.extensionOussama.model.Batiment;
import com.genieLogiciel.Umons.extensionOussama.model.Office;
import com.genieLogiciel.Umons.extensionOussama.model.Room;
import com.genieLogiciel.Umons.extensionOussama.model.RoomStatus;
import com.genieLogiciel.Umons.extensionOussama.repository.BatimentRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.OfficeRepository;
import com.genieLogiciel.Umons.extensionOussama.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for managing rooms in the system.
 */
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BatimentRepository batimentRepository;
    private final BatimentService batimentService;
    private final OfficeRepository officeRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, BatimentRepository batimentRepository, BatimentService batimentService, OfficeRepository officeRepository) {
        this.roomRepository = roomRepository;
        this.batimentRepository = batimentRepository;
        this.batimentService = batimentService;
        this.officeRepository = officeRepository;
    }

    /**
     * Adds a new room to the system.
     * Checks if the associated building exists and if it is available.
     * Updates the building and office information if applicable.
     *
     * @param newRoom The room to add.
     * @return An Optional containing the added room if successful.
     *         Returns an empty Optional if the building is not found.
     */
    public Optional<Room> addRoom(Room newRoom) {
        String buildingName = newRoom.getAssociatedBuilding();
        Optional<Batiment> batimentOptional = batimentRepository.findByName(buildingName);

        if (batimentOptional.isPresent()) {
            Batiment batiment = batimentOptional.get();
            if (batiment.getAvailable()) {
                roomRepository.save(newRoom);
                newRoom.setName(newRoom.getAssociatedBuilding() + "-" + newRoom.getId());
                roomRepository.save(newRoom);
                if (newRoom.getType().equals("OFFICE")) {
                    Office newOffice = new Office();
                    newOffice.setName(newRoom.getName());
                    newOffice.setAvailable(newRoom.getAvailable());
                    newOffice.setBatiment(newRoom.getAssociatedBuilding());
                    batiment.getAllOffices().add(newOffice.getName());
                    batiment.setNumbersOfOffices(batiment.getNumbersOfOffices() + 1);
                    newOffice.setLocation(newRoom.getAssociatedBuilding() + " , " + batiment.getAdresse());
                    officeRepository.save(newOffice);
                    batimentRepository.save(batiment);
                }
                batimentService.addRoomToBuild(newRoom.getName());
                return Optional.of(newRoom);
            } else {
                newRoom.setStatus(RoomStatus.UNAVAILABLE);
                newRoom.setAvailable(false);
                roomRepository.save(newRoom);
                newRoom.setName(newRoom.getAssociatedBuilding() + "-" + newRoom.getId());
                roomRepository.save(newRoom);
                if (newRoom.getType().equals("OFFICE")) {
                    Office newOffice = new Office();
                    newOffice.setName(newRoom.getName());
                    newOffice.setAvailable(newRoom.getAvailable());
                    newOffice.setBatiment(newRoom.getAssociatedBuilding());
                    batiment.getAllOffices().add(newOffice.getName());
                    batiment.setNumbersOfOffices(batiment.getNumbersOfOffices() + 1);
                    newOffice.setLocation(newRoom.getAssociatedBuilding() + " , " + batiment.getAdresse());
                    officeRepository.save(newOffice);
                    batimentRepository.save(batiment);
                }
                batimentService.addRoomToBuild(newRoom.getName());
                return Optional.of(newRoom);
            }
        } else {
            return Optional.empty(); // Building not found
        }
    }

    /**
     * Retrieves details of a room by its ID.
     *
     * @param id The ID of the room.
     * @return An Optional containing the room if found.
     *         Returns an empty Optional if the room is not found.
     */
    public Optional<Room> getRoomDetails(Long id) {
        return roomRepository.findById(id);
    }

    /**
     * Adds new equipment to a room.
     *
     * @param id        The ID of the room.
     * @param equipments The list of equipment to add.
     * @return An Optional containing the updated room if successful.
     *         Returns an empty Optional if the room is not found.
     */
    public Optional<Room> addEquipments(Long id, List<String> equipments) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.getEquipement().addAll(equipments);
            return Optional.of(roomRepository.save(room));
        }
        return Optional.empty();
    }

    /**
     * Retrieves rooms associated with a specific building.
     *
     * @param buildingName The name of the building.
     * @return A list of rooms associated with the specified building.
     *         If the building name is null or empty, returns all rooms.
     */
    public List<Room> getRoomByBuilding(String buildingName) {
        if (buildingName == null || buildingName.isEmpty()) {
            return roomRepository.findAll();
        } else {
            return roomRepository.findByAssociatedBuilding(buildingName);
        }
    }

    /**
     * Retrieves rooms of a specific type.
     *
     * @param type The type of rooms to retrieve.
     * @return A list of rooms of the specified type.
     *         If the type is null or empty, returns all rooms.
     */
    public List<Room> getRoomsByType(String type) {
        if (type == null || type.isEmpty()) {
            return roomRepository.findAll();
        } else {
            return roomRepository.findByType(type);
        }
    }

    /**
     * Retrieves rooms based on building name and type.
     *
     * @param buildingName The name of the building.
     * @param type         The type of rooms.
     * @return A list of rooms that match the specified building name and type.
     *         If both parameters are null or empty, returns all rooms.
     */
    public List<Room> getRooms(String buildingName, String type) {
        if ((buildingName == null || buildingName.isEmpty()) && (type == null || type.isEmpty())) {
            return roomRepository.findAll();
        } else if (buildingName == null || buildingName.isEmpty()) {
            return roomRepository.findByType(type);
        } else if (type == null || type.isEmpty()) {
            return roomRepository.findByAssociatedBuilding(buildingName);
        } else {
            return roomRepository.findByAssociatedBuildingAndType(buildingName, type);
        }
    }

    /**
     * Updates the status of a room by its name.
     *
     * @param roomName The name of the room.
     * @param status   The new status to set.
     * @return The updated room if successful.
     *         Returns null if the room is not found or if the status cannot be set.
     */
    public Room updateRoomStatus(String roomName, RoomStatus status) {
        Optional<Room> roomOptional = roomRepository.getRoomByName(roomName);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setStatus(status);
            if (status == RoomStatus.UNAVAILABLE) {
                room.setAvailable(false);
            } else if (status == RoomStatus.AVAILABLE) {
                Optional<Batiment> batOp = batimentRepository.findByName(room.getAssociatedBuilding());
                if (batOp.isPresent()) {
                    Batiment bat = batOp.get();
                    if (bat.getAvailable()) {
                        room.setAvailable(true);
                    } else {
                        return null;
                    }
                }
            }
            return roomRepository.save(room);
        }
        return null;
    }

    /**
     * Updates the equipment list of a room by its name.
     *
     * @param roomName   The name of the room.
     * @param equipments The new list of equipment to set.
     * @return The updated room if successful.
     *         Returns null if the room is not found.
     */
    public Room updateRoomEquipments(String roomName, List<String> equipments) {
        Optional<Room> roomOptional = roomRepository.getRoomByName(roomName);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setEquipement(equipments);
            return roomRepository.save(room);
        }
        return null;
    }

    /**
     * Updates the availability of a room by its name.
     *
     * @param roomName    The name of the room.
     * @param availability The new availability status to set.
     * @return The updated room if successful.
     *         Returns null if the room is not found.
     */
    public Room updateRoomAvailability(String roomName, Boolean availability) {
        Optional<Room> roomOptional = roomRepository.getRoomByName(roomName);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setAvailable(availability);
            return roomRepository.save(room);
        }
        return null;
    }

    /**
     * Deletes a room by its name.
     *
     * @param name The name of the room to delete.
     * @return true if the room was successfully deleted, false otherwise.
     */
    public boolean deleteRoomByName(String name) {
        Optional<Room> roomOptional = roomRepository.getRoomByName(name);
        if (roomOptional.isPresent()) {
            roomRepository.deleteByName(name);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all rooms.
     *
     * @return A list of all rooms.
     */
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
