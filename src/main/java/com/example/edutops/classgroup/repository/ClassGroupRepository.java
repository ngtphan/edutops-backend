package com.example.edutops.classgroup.repository;

import com.example.edutops.classgroup.entity.ClassGroup;
import com.example.edutops.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassGroupRepository extends BaseRepository<ClassGroup> {

    Optional<ClassGroup> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT COUNT(cg) > 0 FROM ClassGroup cg WHERE cg.code = :code AND cg.publicId <> :publicId")
    boolean existsByCodeAndPublicIdNot(@Param("code") String code, @Param("publicId") UUID publicId);
}
