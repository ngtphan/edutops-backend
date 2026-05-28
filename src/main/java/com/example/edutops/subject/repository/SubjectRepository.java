package com.example.edutops.subject.repository;

import com.example.edutops.common.repository.BaseRepository;
import com.example.edutops.subject.entity.Subject;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends BaseRepository<Subject> {
    
    Optional<Subject> findByCode(String code);

    boolean existsByCode(String code);
}
