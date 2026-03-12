package com.verizon.verizon.dtos.response;

import com.verizon.verizon.dtos.entities_dto.UserDTO;

import java.time.LocalDateTime;

public class AuthResAndSecDTOFactory {
    private AuthResAndSecDTOFactory(){
        throw new IllegalArgumentException("you can not create an instance with this private constructor");
    }

    // FACTORY CONSTRUCTOR FOR SIGN IN METHOD
    public static AuthResponseDTO forSignIn(LocalDateTime lastLogin,
                                            String message,
                                            String accessToken,
                                            UserDTO userDTO
    ){
        return  new AuthResponseDTO.Builder(accessToken,message)
                .userDTO(userDTO)
                .lastLogin(lastLogin)
                .build();
    }

    // FACTORY CONSTRUCTOR FOR SECURITY CHALLENGE METHOD
    public static AuthResponseDTO forSecurityChallenge(String accessToken,String message,
                                                       UserDTO userDTO,
                                                       SecurityDataResponseDto securityDataResponseDto){

        return new AuthResponseDTO.Builder(accessToken,message)
                .userDTO(userDTO)
                .securityDataResponseDto(securityDataResponseDto)
                .build();

    }

    // FACTORY CONSTRUCTOR FOR REGISTRATION METHOD
    public static AuthResponseDTO forSignUp(LocalDateTime createdAt,
                                            String verificationToken,
                                            boolean requiresVerification,
                                            String message,
                                            String accessToken,
                                            SecurityDataResponseDto securityDataResponseDto,
                                            UserDTO userDTO,
                                            boolean isActive) {
        return new AuthResponseDTO.Builder(accessToken, message)
                .isActive(isActive)
                .verificationToken(verificationToken)
                .requiresVerification(requiresVerification)
                .securityDataResponseDto(securityDataResponseDto)
                .userDTO(userDTO)
                .createdAt(createdAt)
                .build();
    }

    // Factory method to create SecurityDataResponseDto object
    public  static SecurityDataResponseDto createSecurityDataResponseDto(String securityQuestion, String message) {
        return new SecurityDataResponseDto.Builder(securityQuestion, message).build();
    }
}
