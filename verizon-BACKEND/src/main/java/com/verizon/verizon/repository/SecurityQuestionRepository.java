package com.verizon.verizon.repository;

import com.verizon.verizon.entity.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Long> {
    Optional<SecurityQuestion> findByQuestionText(String question);
    Optional<SecurityQuestion> findByName(String name);  // ← Add this
    boolean existsByQuestionText(String question);

}
