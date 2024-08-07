package com.genieLogiciel.Umons.extensionOussama.repository;

import com.genieLogiciel.Umons.extensionOussama.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {

    Optional<Room> getRoomByName (String name);
    List<Room> findByAssociatedBuilding(String buildingName);
    void deleteByName(String name);

    List<Room> findByType(String type);
    List<Room> findByAssociatedBuildingAndType(String associatedBuilding, String type);


}
