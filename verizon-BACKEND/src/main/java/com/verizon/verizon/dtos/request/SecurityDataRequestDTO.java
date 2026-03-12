package com.verizon.verizon.dtos.request;


public class SecurityDataRequestDTO {
    private final String securityQuestion;
    private final String securityAnswer;

    public SecurityDataRequestDTO(String securityQuestion, String  securityAnswer){
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

}
