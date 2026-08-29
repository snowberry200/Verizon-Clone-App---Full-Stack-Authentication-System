package com.verizon.verizon.dtos.request;

import com.verizon.verizon.constant.Validator;

public class AuthReqAndSecDTOFactory {
    private AuthReqAndSecDTOFactory(){
        throw new IllegalArgumentException("This is a utility class and cannot be instantiated");
    }
    // FACTORY CONSTRUCTOR FOR SIGN IN METHOD
    public static AuthRequestDTO forSignIn(String email,
                                           String password){
        // Validate required fields
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required for sign in");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required for sign in");
        }
        String statusCode = "NONACTIVE";
        return new AuthRequestDTO.Builder(email,password,null)
                .statusCode(statusCode)
                .build();

    }

    // FACTORY CONSTRUCTOR FOR SECURITY CHALLENGE METHOD
    public static AuthRequestDTO forSecurityChallenge(
            String email,
            SecurityDataRequestDTO securityDataRequestDTO) {
        // Validate required fields
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required for security challenge");
        }
        if (securityDataRequestDTO == null) {
            throw new IllegalArgumentException("Security data is required for security challenge");
        }
        if (securityDataRequestDTO.getSecurityQuestion() == null ||
                securityDataRequestDTO.getSecurityQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Security question is required");
        }
        if (securityDataRequestDTO.getSecurityAnswer() == null ||
                securityDataRequestDTO.getSecurityAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("Security answer is required");
        }
        String statusCode = "PENDING_SECURITY";
        return new AuthRequestDTO.Builder(email, null, null)
                .statusCode(statusCode)// No password needed
                .securityDataRequestDTO(securityDataRequestDTO)
                .build();
    }

    // FACTORY CONSTRUCTOR FOR REGISTRATION METHOD
    public static AuthRequestDTO forSignUp(String email,
                                           String password,
                                           String name,
                                           String statusCode,
                                           SecurityDataRequestDTO securityDataRequestDTO){

        // Validate all required fields
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required for registration");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required for registration");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required for registration");
        }
        if (securityDataRequestDTO == null) {
            throw new IllegalArgumentException("Security data is required for registration");
        }
        if (securityDataRequestDTO.getSecurityQuestion() == null ||
                securityDataRequestDTO.getSecurityQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Security question is required for registration");
        }
        if (securityDataRequestDTO.getSecurityAnswer() == null ||
                securityDataRequestDTO.getSecurityAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("Security answer is required for registration");
        }

        // Use provided statusCode or default to NONACTIVE
        String finalStatusCode = (statusCode != null && !statusCode.trim().isEmpty())
                ? statusCode
                : Validator.NONACTIVE;
        return new AuthRequestDTO.Builder(email,password,name)
                .statusCode(finalStatusCode)
                .securityDataRequestDTO(securityDataRequestDTO)
                .build();

    }
}
