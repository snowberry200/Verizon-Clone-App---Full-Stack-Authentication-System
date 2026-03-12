package com.verizon.verizon.service;

import com.verizon.verizon.entity.SecurityQuestion;
import com.verizon.verizon.repository.SecurityQuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class QuestionDataLoader implements CommandLineRunner {
    private final SecurityQuestionRepository securityQuestionRepository;

    public QuestionDataLoader(SecurityQuestionRepository securityQuestionRepository) {
        this.securityQuestionRepository = securityQuestionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(securityQuestionRepository.count() == 0){
            List<QuestionPair> questions = Arrays.asList(
                    new QuestionPair("PET_NAME", "What was your first pet's name?"),
                    new QuestionPair("MOTHER_MAIDEN", "What is your mother's maiden name?"),
                    new QuestionPair("FIRST_SCHOOL", "What was your first school?"),
                    new QuestionPair("BIRTH_CITY", "What city were you born in?"),
                    new QuestionPair("FAVORITE_BOOK", "What is your favorite book?")
            );

            questions.forEach(pair -> {
                SecurityQuestion securityQuestion = new SecurityQuestion();
                securityQuestion.setName(pair.name);           // ← Set short code
                securityQuestion.setQuestionText(pair.text);    // ← Set full question
                securityQuestionRepository.save(securityQuestion);
            });
        }
    }

    // Helper class
    private static class QuestionPair {
        String name;
        String text;

        QuestionPair(String name, String text) {
            this.name = name;
            this.text = text;
        }
    }
}