package com.verizon.verizon.dtos.entities_dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.verizon.verizon.entity.Roles;
import com.verizon.verizon.entity.User;
import com.verizon.verizon.entity.UserSecurityQuestion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDTO {
    private final Long id;
    private final String email;
    private final String name;
    private final String statusCode;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastLogin;
    @JsonIgnore
    public List<RolesDTO> rolesDTOS;
    private String accessToken;
    private final String verificationToken;
    private LocalDateTime verificationTokenExpiry;
    private LocalDateTime verifiedAt;
    @JsonIgnore
    private UserSecurityQuestionDTO userSecurityQuestionDTO;

    public UserDTO(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.name = builder.name;
        this.statusCode = builder.statusCode;
        this.createdAt = builder.createdAt;
        this.lastLogin = builder.lastLogin;
        this.rolesDTOS = builder.rolesDTOS;
        this.accessToken = builder.accessToken;
        this.verificationToken = builder.verificationToken;
        this.verificationTokenExpiry = builder.verificationTokenExpiry;
        this.verifiedAt = builder.verifiedAt;
        this.userSecurityQuestionDTO = builder.userSecurityQuestionDTO;

    }

    // create method to add single RoleDTO
    public void addSingleRoleDTO(RolesDTO rolesDTO) {
        if (this.rolesDTOS == null) {
            this.rolesDTOS = new ArrayList<>();
        }
        if (!this.rolesDTOS.contains(rolesDTO)) {
            this.rolesDTOS.add(rolesDTO);
           // rolesDTO.addSingleUserDTO(this); // ← Maintain the OTHER side!
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
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

    public String getStatusCode() {
        return statusCode;
    }
    public List<RolesDTO> getRolesDTOS() {
        return rolesDTOS;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public LocalDateTime getVerificationTokenExpiry() {
        return verificationTokenExpiry;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
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
        private String verificationToken;
        private LocalDateTime verificationTokenExpiry;
        private LocalDateTime verifiedAt;
        private final String statusCode;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime lastLogin = LocalDateTime.now();
        private List<RolesDTO> rolesDTOS = new ArrayList<>();
        private UserSecurityQuestionDTO userSecurityQuestionDTO = null;

        //constructor for primitive required params
        public Builder(Long id, String email, String name, String accessToken, String statusCode) {
            // Validate required fields
            this.id = Objects.requireNonNull(id, "ID cannot be null");
            this.email = Objects.requireNonNull(email, "Email cannot be null");
            this.name = Objects.requireNonNull(name, "Name cannot be null");
            this.accessToken = accessToken;  // Can be null for unverified users
            this.statusCode = Objects.requireNonNull(statusCode, "Status code cannot be null");

            // Set defaults for optional fields
            this.createdAt = LocalDateTime.now();
            this.lastLogin = LocalDateTime.now();
            this.rolesDTOS = new ArrayList<>();
            this.userSecurityQuestionDTO = null;
            this.verificationToken = null;
            this.verificationTokenExpiry = null;
            this.verifiedAt = null;
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

        public Builder verificationToken(String verificationToken) {
            this.verificationToken = verificationToken;
            return this;
        }

        public Builder verificationTokenExpiry(LocalDateTime verificationTokenExpiry) {
            this.verificationTokenExpiry = verificationTokenExpiry;
            return this;
        }

        public Builder verifiedAt(LocalDateTime verifiedAt) {
            this.verifiedAt = verifiedAt;
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
        UserDTO newUserDTO = new UserDTO.Builder(user.getId(), user.getEmail(), user.getName(), user.getAccessToken(), user.getStatusCode())
                .createdAt(user.getCreatedAt())
                .verificationToken(user.getVerificationToken())
                .verificationTokenExpiry(user.getVerificationTokenExpiry())
                .verifiedAt(user.getVerifiedAt())
                .lastLogin(user.getLastLogin())
                .build();
        //convert roles to rolesDTO
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            List<Roles> roles = user.getRoles();
            roles.stream().map(role -> {
                RolesDTO rolesDTO = RolesDTO.convertRolesToDTO(role);
                newUserDTO.addSingleRoleDTO(rolesDTO);
                return newUserDTO;
            }).collect(Collectors.toList());
        } else {
            newUserDTO.setRolesDTOS(new ArrayList<>());

        }
        //validate newUserSecurityQuestion
        if (user.getUserSecurityQuestion() != null) {
            UserSecurityQuestion newUserSecurityQuestion = user.getUserSecurityQuestion();
            UserSecurityQuestionDTO newUserSecurityQuestionDTO = UserSecurityQuestionDTO.convertUserSecurityQuestionDTO(newUserSecurityQuestion);
            newUserDTO.setUserSecurityQuestionDTO(newUserSecurityQuestionDTO);
        }
        return newUserDTO;

    }

}
