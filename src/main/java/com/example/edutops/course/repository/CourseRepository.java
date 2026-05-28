package com.example.edutops.course.repository;

import com.example.edutops.common.repository.BaseRepository;
import com.example.edutops.course.entity.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends BaseRepository<Course> {

    Optional<Course> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.code = :code AND c.publicId <> :publicId")
    boolean existsByCodeAndPublicIdNot(@Param("code") String code, @Param("publicId") UUID publicId);
}
