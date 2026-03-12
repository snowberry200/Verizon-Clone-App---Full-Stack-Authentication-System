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
                                        boolean isActive,
                                        LocalDateTime createdAt,
                                        LocalDateTime lastLogin,
                                        List<RolesDTO> rolesDTOS,
                                        UserSecurityQuestionDTO userSecurityQuestionDTO) {
        UserDTO newUserDTO = new UserDTO.Builder(id,
                name, email, accessToken)
                .isActive(isActive)
                .createdAt(createdAt)
                .lastLogin(lastLogin)
                .rolesDTOS(rolesDTOS)
                .userSecurityQuestionDTO(userSecurityQuestionDTO)
                .build();

        if (rolesDTOS != null) {
            for (RolesDTO rolesDTO : rolesDTOS) {
                rolesDTO.addSingleUserDTO(newUserDTO);
            }
        }
        return newUserDTO;

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


    //factory constructor to create new userDTO
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
