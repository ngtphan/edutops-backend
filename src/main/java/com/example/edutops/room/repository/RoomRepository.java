package com.example.edutops.room.repository;

import com.example.edutops.common.repository.BaseRepository;
import com.example.edutops.room.entity.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends BaseRepository<Room> {

    Optional<Room> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT COUNT(r) > 0 FROM Room r WHERE r.name = :name AND r.publicId <> :publicId")
    boolean existsByNameAndPublicIdNot(@Param("name") String name, @Param("publicId") UUID publicId);
}
