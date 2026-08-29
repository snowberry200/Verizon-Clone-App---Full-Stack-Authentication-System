package com.verizon.verizon.dtos.entities_dto;

import java.time.LocalDateTime;
import java.util.List;


public class CreateDTOFactory {

    //private constructor to prevent instantiation
    private CreateDTOFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    //factory constructor to create UserDTO
    public static UserDTO createUserDTO(Long id,
                                        String email,
                                        String name,
                                        String accessToken,
                                        String verificationToken,
                                        LocalDateTime verificationTokenExpiry,
                                        LocalDateTime verifiedAt,
                                        String statusCode,
                                        LocalDateTime createdAt,
                                        LocalDateTime lastLogin,
                                        List<RolesDTO> rolesDTOS,
                                        UserSecurityQuestionDTO userSecurityQuestionDTO) {
        // Validate required fields
        if (id == null || email == null || name == null || accessToken == null || statusCode == null) {
            throw new IllegalArgumentException("Required fields cannot be null");
        }
        UserDTO.Builder builder = new UserDTO.Builder(id,
                name, email, accessToken, statusCode)
                .createdAt(createdAt)
                .lastLogin(lastLogin)
                .verificationToken(verificationToken)
                .verificationTokenExpiry(verificationTokenExpiry)
                .verifiedAt(verifiedAt)
                .rolesDTOS(rolesDTOS)
                .userSecurityQuestionDTO(userSecurityQuestionDTO);

        UserDTO newUserDTO = builder.build();

        if (rolesDTOS != null) {
            for (RolesDTO rolesDTO : rolesDTOS) {
                rolesDTO.addSingleUserDTO(newUserDTO);
            }
        }
        return newUserDTO;


    }

    // Overloaded method for creating unverified user DTO (during registration)
    public static UserDTO createUnverifiedUserDTO(Long id,
                                                  String email,
                                                  String name,
                                                  String verificationToken,
                                                  LocalDateTime verificationTokenExpiry,
                                                  String statusCode,
                                                  LocalDateTime createdAt,
                                                  UserSecurityQuestionDTO userSecurityQuestionDTO) {

        return createUserDTO(
                id,                          // Long id
                email,                       // String email
                name,                        // String name
                null,                        // String accessToken (null for unverified)
                verificationToken,           // String verificationToken
                verificationTokenExpiry,     // LocalDateTime verificationTokenExpiry
                null,                        // LocalDateTime verifiedAt (null for unverified)
                statusCode,                  // String statusCode
                createdAt,                   // LocalDateTime createdAt
                null,                        // LocalDateTime lastLogin (null for unverified)
                null,                        // List<RolesDTO> rolesDTOS
                userSecurityQuestionDTO      // UserSecurityQuestionDTO
        );
    }

    // Create verified user DTO (after email verification)
    public static UserDTO createVerifiedUserDTO(Long id,
                                                String email,
                                                String name,
                                                String accessToken,
                                                LocalDateTime verifiedAt,
                                                String statusCode,
                                                LocalDateTime createdAt,
                                                LocalDateTime lastLogin,
                                                List<RolesDTO> rolesDTOS,
                                                UserSecurityQuestionDTO userSecurityQuestionDTO) {

        return createUserDTO(
                id,                          // Long id
                email,                       // String email
                name,                        // String name
                accessToken,                 // String accessToken
                null,                        // String verificationToken (cleared)
                null,                        // LocalDateTime verificationTokenExpiry (cleared)
                verifiedAt,                  // LocalDateTime verifiedAt
                statusCode,                  // String statusCode (should be "ACTIVE")
                createdAt,                   // LocalDateTime createdAt
                lastLogin,                   // LocalDateTime lastLogin
                rolesDTOS,                   // List<RolesDTO> rolesDTOS
                userSecurityQuestionDTO      // UserSecurityQuestionDTO
        );
    }


    // Create simple user DTO for testing or basic cases
    public static UserDTO createSimpleUserDTO(Long id,
                                              String email,
                                              String name,
                                              String statusCode) {
        return createUserDTO(
                id, email, name,
                null,                        // No access token
                null,                        // No verification token
                null,                        // No token expiry
                null,                        // Not verified
                statusCode,                  // statusCode
                LocalDateTime.now(),         // createdAt
                null,                        // No lastLogin
                null,                        // No roles
                null                         // No security question
        );
    }


    //factory constructor to create SecurityQuestionDTO
    public static SecurityQuestionDTO createSecurityQuestionDTO(Long id, String questionText, String name) {

        // Add validation
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Question name cannot be empty");
        }

        return new SecurityQuestionDTO.Builder(id, questionText, name).build();
    }


    //factory constructor to create new roleDTO
    public static RolesDTO createRolesDTO(String name, Long id, List<UserDTO> userDTOS) {
        RolesDTO newRolesDTO = new RolesDTO.Builder(name, id)
                .userDTO(userDTOS)
                .build();
        if (userDTOS != null) {
            for (UserDTO userDTO : userDTOS) {
                userDTO.addSingleRoleDTO(newRolesDTO);
            }
        }
        return newRolesDTO;


    }

    //factory constructor to create UserSecurityQuestionDTO
    public static UserSecurityQuestionDTO createUserSecurityQuestionDTO(Long id, String answer, SecurityQuestionDTO securityQuestionDTO, List<UserDTO> userDTOS) {

        UserSecurityQuestionDTO userSecurityQuestionDTO = new UserSecurityQuestionDTO.Builder(id, answer)
                .securityQuestionDTO(securityQuestionDTO)
                .userDTO(userDTOS)
                .build();

        // Validate required fields before building
        if (securityQuestionDTO == null) {
            throw new IllegalArgumentException("SecurityQuestionDTO is required");
        }
        // Maintain bidirectional relationship with UserDTO
        for (UserDTO userDTO : userDTOS) {
            if (userDTO.getUserSecurityQuestionDTO() != userSecurityQuestionDTO) {
                userDTO.setUserSecurityQuestionDTO(userSecurityQuestionDTO);
            }
        }

        return userSecurityQuestionDTO;
    }

}
