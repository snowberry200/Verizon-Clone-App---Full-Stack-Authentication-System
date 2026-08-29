package com.verizon.verizon.repository;

import com.verizon.verizon.entity.UserSecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserSecurityQuestionRepository extends JpaRepository<UserSecurityQuestion, Long> {
    boolean existsByUsersId(Long userId);
    Optional<UserSecurityQuestion> findByUsersId(Long userId);
}
