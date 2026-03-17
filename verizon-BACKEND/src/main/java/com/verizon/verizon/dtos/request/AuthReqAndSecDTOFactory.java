package com.verizon.verizon.dtos.request;

import com.verizon.verizon.userstatuses.ActiveStatus;
import com.verizon.verizon.userstatuses.NonActiveStatus;
import com.verizon.verizon.userstatuses.UserStatus;

public class AuthReqAndSecDTOFactory {
    private AuthReqAndSecDTOFactory(){
        throw new IllegalArgumentException(" instance can not be created for this private constructor");
    }
    // FACTORY CONSTRUCTOR FOR SIGN IN METHOD
    public static AuthRequestDTO forSignIn(String email,
                                           String password){
        return new AuthRequestDTO.Builder(email,password,"")
                .status(new NonActiveStatus())
                .build();

    }

    // FACTORY CONSTRUCTOR FOR SECURITY CHALLENGE METHOD
    public static AuthRequestDTO forSecurityChallenge(
            String email,
            SecurityDataRequestDTO securityDataRequestDTO) {
        return new AuthRequestDTO.Builder(email, "", "")  // No password needed
                .securityDataRequestDTO(securityDataRequestDTO)
                .build();
    }

    // FACTORY CONSTRUCTOR FOR REGISTRATION METHOD
    public static AuthRequestDTO forSignUp(String email,
                                           String password,
                                           String name,
                                           UserStatus status,
                                           SecurityDataRequestDTO securityDataRequestDTO){

        //validate securityDataRequestDTO because it is a required dependent param
        if(securityDataRequestDTO==null ){
            throw new IllegalArgumentException("Security data is required for registration");
        }
        if (securityDataRequestDTO.getSecurityQuestion() == null || securityDataRequestDTO.getSecurityQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Security question is required for registration");
        }
        if (securityDataRequestDTO.getSecurityAnswer() == null || securityDataRequestDTO.getSecurityAnswer().trim().isEmpty()) {
            throw new IllegalArgumentException("Security answer is required for registration");
        }
        return new AuthRequestDTO.Builder(email,password,name)
                .status(new ActiveStatus())
                .securityDataRequestDTO(securityDataRequestDTO)
                .build();

    }
}
