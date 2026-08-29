package com.verizon.verizon.dtos.response;

public class SecurityDataResponseDto {
    private String securityQuestion;
    private String message;  // Instead of answer


    public SecurityDataResponseDto() {
        //for Jackson
    }

    private SecurityDataResponseDto(Builder builder) {
        this.securityQuestion = builder.securityQuestion;
        this.message = builder.message;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Builder {
        private final String securityQuestion;
        private final String message;

        public Builder(String securityQuestion, String message) {
            this.securityQuestion = securityQuestion;
            this.message = message;
        }

        public SecurityDataResponseDto build() {
            return new SecurityDataResponseDto(this);
        }
    }


}