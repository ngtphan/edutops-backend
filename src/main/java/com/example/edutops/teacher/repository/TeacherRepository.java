package com.example.edutops.teacher.repository;

import com.example.edutops.common.repository.BaseRepository;
import com.example.edutops.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends BaseRepository<Teacher> {

    @Query("SELECT t FROM Teacher t WHERE t.user.publicId = :userPublicId")
    Optional<Teacher> findByUserPublicId(@Param("userPublicId") UUID userPublicId);
}
