package com.example.edutops.user.repository;

import com.example.edutops.common.repository.BaseRepository;
import com.example.edutops.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
