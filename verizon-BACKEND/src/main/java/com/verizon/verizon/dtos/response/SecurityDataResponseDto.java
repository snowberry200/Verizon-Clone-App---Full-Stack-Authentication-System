package com.verizon.verizon.dtos.response;

public class SecurityDataResponseDto {
    private final String securityQuestion;
    private final String message;  // Instead of answer

    private SecurityDataResponseDto(Builder builder) {
        this.securityQuestion = builder.securityQuestion;
        this.message = builder.message;
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

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getMessage() {
        return message;
    }


}