package com.verizon.verizon.dtos.entities_dto;

import com.verizon.verizon.entity.SecurityQuestion;

public class SecurityQuestionDTO {
    private final Long id;
    private final String questionText;
    private final String name;

    public SecurityQuestionDTO(Builder builder) {
        this.id = builder.id;
        this.questionText = builder.questionText;
        this.name = builder.name;
    }

    public static class Builder {
        private final Long id;
        private final String questionText;
        private final String name;

        public Builder(Long id, String questionText, String name) {
            this.id = id;
            this.questionText = questionText;
            this.name = name;
        }

        public SecurityQuestionDTO build() {
            return new SecurityQuestionDTO(this);
        }
    }


    // Getters
    public Long getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getName() {
        return name;
    }

    // method to convert SecurityQuestion To DTO
    public static SecurityQuestionDTO convertSecurityQuestionToDTO(SecurityQuestion securityQuestion){
        return new SecurityQuestionDTO.Builder(securityQuestion.getId(),securityQuestion.getQuestionText(),securityQuestion.getName())
                .build();

    }
}