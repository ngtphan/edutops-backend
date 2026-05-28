package com.example.edutops.student.repository;

import com.example.edutops.common.repository.BaseRepository;
import com.example.edutops.student.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends BaseRepository<Student> {

    @Query("SELECT s FROM Student s WHERE s.user.publicId = :userPublicId")
    Optional<Student> findByUserPublicId(@Param("userPublicId") UUID userPublicId);
}
