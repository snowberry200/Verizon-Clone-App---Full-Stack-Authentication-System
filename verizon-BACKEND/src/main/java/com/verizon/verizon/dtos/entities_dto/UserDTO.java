package com.verizon.verizon.dtos.entities_dto;

import com.verizon.verizon.entity.Roles;
import com.verizon.verizon.entity.User;
import com.verizon.verizon.entity.UserSecurityQuestion;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    public final Long id;
    public final String email;
    public final String name;
    private final boolean isActive;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastLogin;
    public List<RolesDTO> rolesDTOS;
    private String accessToken;
    private UserSecurityQuestionDTO userSecurityQuestionDTO;

    public UserDTO(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.name = builder.name;
        this.isActive = builder.isActive;
        this.createdAt = builder.createdAt;
        this.lastLogin = builder.lastLogin;
        this.rolesDTOS = builder.rolesDTOS;
        this.accessToken = builder.accessToken;
        this.userSecurityQuestionDTO = builder.userSecurityQuestionDTO;

    }

    // create method to add single RoleDTO
    public void addSingleRoleDTO(RolesDTO rolesDTO) {
        if (this.rolesDTOS == null) {
            this.rolesDTOS = new ArrayList<>();
        }
        if (!this.rolesDTOS.contains(rolesDTO)) {
            this.rolesDTOS.add(rolesDTO);
            rolesDTO.addSingleUserDTO(this); // ← Maintain the OTHER side!
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public List<RolesDTO> getRolesDTOS() {
        return rolesDTOS;
    }

    public void setRolesDTOS(List<RolesDTO> rolesDTOS) {
        this.rolesDTOS = rolesDTOS;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserSecurityQuestionDTO getUserSecurityQuestionDTO() {
        return userSecurityQuestionDTO;
    }

    public void setUserSecurityQuestionDTO(UserSecurityQuestionDTO userSecurityQuestionDTO) {
        this.userSecurityQuestionDTO = userSecurityQuestionDTO;
    }

    public static class Builder {
        private final Long id;
        private final String email;
        private final String name;
        private final String accessToken;
        private boolean isActive = false;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime lastLogin = LocalDateTime.now();
        private List<RolesDTO> rolesDTOS = new ArrayList<>();
        private UserSecurityQuestionDTO userSecurityQuestionDTO = null;

        //constructor for primitive required params
        public Builder(Long id, String email, String name, String accessToken) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.accessToken = accessToken;
        }

        // constructor for primitive optional params
        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        // constructor for dependent non-primitive params
        public Builder rolesDTOS(List<RolesDTO> rolesDTOS) {
            this.rolesDTOS = rolesDTOS;
            return this;
        }

        public Builder userSecurityQuestionDTO(UserSecurityQuestionDTO userSecurityQuestionDTO) {
            this.userSecurityQuestionDTO = userSecurityQuestionDTO;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }


    }

    public static UserDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO newUserDTO = new UserDTO.Builder(user.getId(), user.getEmail(), user.getName(), user.getAccessToken())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();
        //convert roles to rolesDTO
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<Roles> roles = user.getRoles();
            roles.stream().map(role -> {
                RolesDTO rolesDTO = RolesDTO.convertRolesToDTO(role);
                newUserDTO.addSingleRoleDTO(rolesDTO);//<- maintaining bi-directional relation
                rolesDTO.addSingleUserDTO(newUserDTO);
                return newUserDTO;
            }).collect(Collectors.toList());
        } else {
            newUserDTO.setRolesDTOS(new ArrayList<>());

        }
        if (user.getUserSecurityQuestion() != null) {
            UserSecurityQuestion newUserSecurityQuestion = user.getUserSecurityQuestion();
            UserSecurityQuestionDTO newUserSecurityQuestionDTO = UserSecurityQuestionDTO.convertUserSecurityQuestionDTO(newUserSecurityQuestion);
            newUserDTO.setUserSecurityQuestionDTO(newUserSecurityQuestionDTO);
        }
        return newUserDTO;

    }

}
