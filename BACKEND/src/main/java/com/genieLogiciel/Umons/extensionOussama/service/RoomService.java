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

    public Optional<Room> addRoom(Room newRoom) {
        String buildingName = newRoom.getAssociatedBuilding();
        Optional<Batiment> batimentOptional = batimentRepository.findByName(buildingName);

        if (batimentOptional.isPresent()) {
            Batiment batiment = batimentOptional.get();
            if (batiment.getAvailable()) {
                roomRepository.save(newRoom);
                newRoom.setName(newRoom.getAssociatedBuilding()+"-"+newRoom.getId());
                roomRepository.save(newRoom);
                System.out.println("le type de la salle est : " + newRoom.getType());
                if (newRoom.getType().equals("OFFICE")){
                    Office newOffice = new Office();
                    newOffice.setName(newRoom.getName());
                    newOffice.setAvailable(newRoom.getAvailable());
                    newOffice.setBatiment(newRoom.getAssociatedBuilding());
                    batiment.getAllOffices().add(newOffice.getName());
                    batiment.setNumbersOfOffices(batiment.getNumbersOfOffices()+1);
                    newOffice.setLocation(newRoom.getAssociatedBuilding() + " , " + batiment.getAdresse());
                    officeRepository.save(newOffice);
                    batimentRepository.save(batiment);
                    System.out.println("type is Office");
                }
                batimentService.addRoomToBuild(newRoom.getName());
                return Optional.of(newRoom);
            } else {
                newRoom.setStatus(RoomStatus.UNAVAILABLE);
                newRoom.setAvailable(false);
                roomRepository.save(newRoom);
                newRoom.setName(newRoom.getAssociatedBuilding()+"-"+newRoom.getId());
                roomRepository.save(newRoom);
                System.out.println("le type de la salle est : " + newRoom.getType());
                if (newRoom.getType().equals("OFFICE")){
                    System.out.println("type is Office");
                    Office newOffice = new Office();
                    newOffice.setName(newRoom.getName());
                    newOffice.setAvailable(newRoom.getAvailable());
                    newOffice.setBatiment(newRoom.getAssociatedBuilding());
                    batiment.getAllOffices().add(newOffice.getName());
                    batiment.setNumbersOfOffices(batiment.getNumbersOfOffices()+1);
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

    public Optional<Room> getRoomDetails(Long id) {
        return roomRepository.findById(id);
    }

    public Optional<Room> addEquipments(Long id, List<String> equipments) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.getEquipement().addAll(equipments);
            return Optional.of(roomRepository.save(room));
        }
        return Optional.empty();
    }

    public List<Room> getRoomByBuilding(String buildingName){
        if (buildingName == null || buildingName.isEmpty()) {
            return roomRepository.findAll();
        } else {
            return roomRepository.findByAssociatedBuilding(buildingName);
        }
    }

    public List<Room> getRoomsByType(String type) {
        if (type == null || type.isEmpty()) {
            return roomRepository.findAll();
        } else {
            return roomRepository.findByType(type);
        }
    }

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


    public Room updateRoomStatus(String roomName, RoomStatus status) {
        Optional<Room> roomOptional = roomRepository.getRoomByName(roomName);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setStatus(status);
            if (status == RoomStatus.UNAVAILABLE){
                room.setAvailable(false);
            }else if (status == RoomStatus.AVAILABLE){
                Optional<Batiment> batOp = batimentRepository.findByName(room.getAssociatedBuilding());
                if (batOp.isPresent()){
                    Batiment bat = batOp.get();
                    if (bat.getAvailable()){
                        room.setAvailable(true);
                    }
                    else {
                        return null;
                    }
                }
            }
            return roomRepository.save(room);
        }
        return null;
    }

    public Room updateRoomEquipments(String roomName, List<String> equipments) {
        Optional<Room> roomOptional = roomRepository.getRoomByName(roomName);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setEquipement(equipments);
            return roomRepository.save(room);
        }
        return null;
    }

    public Room updateRoomAvailability(String roomName, Boolean availability) {
        Optional<Room> roomOptional = roomRepository.getRoomByName(roomName);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setAvailable(availability);
            return roomRepository.save(room);
        }
        return null;
    }

    public boolean deleteRoomByName(String name) {
        Optional<Room> roomOptional = roomRepository.getRoomByName(name);
        if (roomOptional.isPresent()) {
            roomRepository.deleteByName(name);
            return true;
        }
        return false;
    }

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }
}
