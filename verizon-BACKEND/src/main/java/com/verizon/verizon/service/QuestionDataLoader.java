package com.verizon.verizon.service;

import com.verizon.verizon.entity.SecurityQuestion;
import com.verizon.verizon.repository.SecurityQuestionRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class QuestionDataLoader {

    private final SecurityQuestionRepository securityQuestionRepository;

    public QuestionDataLoader(SecurityQuestionRepository securityQuestionRepository) {
        this.securityQuestionRepository = securityQuestionRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadQuestions() {
        try {
            System.out.println("🔍 Loading security questions...");

            if(securityQuestionRepository.count() == 0){
                List<QuestionPair> questions = Arrays.asList(
                        new QuestionPair("CONCERT_ATTENDED", "What was the first live concert you attended?"),
                        new QuestionPair("FIRST_MEET", "Where did you and your spouse first meet?"),
                        new QuestionPair("FAVORITE PLACE", "What was your favorite place to visit as a child?"),
                        new QuestionPair("FIRST_ROOM", "What was the first name of your first roommate?"),
                        new QuestionPair("MEMORABLE_PLACE", "What is the name of a memorable place you visited?"),
                        new QuestionPair("FAVORITE_RESTAURANT", "What was your favorite restaurant in college?")
                );

                questions.forEach(pair -> {
                    SecurityQuestion securityQuestion = new SecurityQuestion();
                    securityQuestion.setName(pair.name);
                    securityQuestion.setQuestionText(pair.text);
                    securityQuestionRepository.save(securityQuestion);
                    System.out.println("✅ Added: " + pair.name);
                });

                System.out.println("✅ Loaded " + securityQuestionRepository.count() + " security questions");
            } else {
                System.out.println("✅ Security questions already exist: " + securityQuestionRepository.count());
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to load security questions: " + e.getMessage());
        }
    }

    private static class QuestionPair {
        String name;
        String text;
        QuestionPair(String name, String text) {
            this.name = name;
            this.text = text;
        }
    }
}