package com.example.edutops.enrollment.repository;

import com.example.edutops.common.repository.BaseRepository;
import com.example.edutops.enrollment.entity.Enrollment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends BaseRepository<Enrollment> {

    boolean existsByStudentIdAndClassGroupId(Long studentId, Long classGroupId);

    List<Enrollment> findByStudentPublicId(UUID studentPublicId);

    @Query("SELECT e FROM Enrollment e WHERE e.classGroup.publicId = :classGroupPublicId")
    List<Enrollment> findByClassGroupPublicId(@Param("classGroupPublicId") UUID classGroupPublicId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.classGroup.id = :classGroupId AND e.status = 'ACTIVE'")
    long countActiveStudentsInClass(@Param("classGroupId") Long classGroupId);
}
