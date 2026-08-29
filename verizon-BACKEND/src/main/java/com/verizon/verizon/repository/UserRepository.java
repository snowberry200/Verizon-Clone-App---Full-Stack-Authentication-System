package com.verizon.verizon.repository;

import com.verizon.verizon.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @NonNull
    Optional<User> findByEmail(String email);
    boolean existsByEmail( String email);
}
