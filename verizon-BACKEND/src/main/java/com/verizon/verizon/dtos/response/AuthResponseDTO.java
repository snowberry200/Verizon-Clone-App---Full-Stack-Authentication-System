package com.verizon.verizon.dtos.response;

import com.verizon.verizon.dtos.entities_dto.UserDTO;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public class AuthResponseDTO {
    private  UserDTO userDTO;
    private  String accessToken;
    private  String message;
    private  String statusCode;
    private  boolean requiresVerification;
    private  String emailVerificationToken;
    private  SecurityDataResponseDto securityDataResponseDto;
    private  LocalDateTime createdAt;
    private  LocalDateTime lastLogin;
    private  LocalDateTime verifiedAt;

    // Default Constructor
    public AuthResponseDTO(){
        //for Jackson
    }

    public AuthResponseDTO(Builder builder) {
        this.userDTO = builder.userDTO;
        this.accessToken = builder.accessToken;
        this.message = builder.message;
        this.statusCode = builder.statusCode;
        this.requiresVerification = builder.requiresVerification;
        this.emailVerificationToken = builder.emailVerificationToken;
        this.createdAt = builder.createdAt;
        this.lastLogin = builder.lastLogin;
        this.securityDataResponseDto = builder.securityDataResponseDto;
        this.verifiedAt = builder.verifiedAt;
    }


    @Nullable
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public boolean isRequiresVerification() {
        return requiresVerification;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    @Nullable
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public static class Builder {
        // Required - always present
        private final String message;

        // Required non-primitive (must be set)
        private UserDTO userDTO;
        private SecurityDataResponseDto securityDataResponseDto;

        // Optional fields
        private String accessToken;
        private String statusCode;
        private boolean requiresVerification;
        private String emailVerificationToken;
        private LocalDateTime verifiedAt;
        private LocalDateTime createdAt;
        private LocalDateTime lastLogin;

        // Constructor - only message is required
        public Builder(String message) {
            // Validate required message
            if (message == null || message.trim().isEmpty()) {
                throw new IllegalArgumentException("Message cannot be null or empty");
            }
            this.message = message;

            // Set defaults for optional fields
            this.accessToken = null;                    // ✅ No token by default
            this.statusCode = "NONACTIVE";
            this.requiresVerification = false;
            this.emailVerificationToken = null;         // ✅ No token by default
            this.verifiedAt = null;
            this.createdAt = LocalDateTime.now();
            this.lastLogin = null;                      // ✅ No loginRecord by default
        }

        // Builder methods for optional fields
        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder statusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder requiresVerification(boolean requiresVerification) {
            this.requiresVerification = requiresVerification;
            return this;
        }

        public Builder emailVerificationToken(String emailVerificationToken) {
            this.emailVerificationToken = emailVerificationToken;
            return this;
        }

        public Builder verifiedAt(LocalDateTime verifiedAt) {
            this.verifiedAt = verifiedAt;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
            return this;
        }

        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public Builder userDTO(UserDTO userDTO) {
            this.userDTO = userDTO;
            return this;
        }

        public Builder securityDataResponseDto(SecurityDataResponseDto securityDataResponseDto) {
            this.securityDataResponseDto = securityDataResponseDto;
            return this;
        }

        public AuthResponseDTO build() {
            // Validate required non-primitive fields
            if (userDTO == null) {
                throw new IllegalStateException("UserDTO is required but was never set");
            }
            if (securityDataResponseDto == null) {
                throw new IllegalStateException("SecurityDataResponseDto is required but was never set");
            }

            // ✅ Validate business logic
            if (requiresVerification && (emailVerificationToken == null || emailVerificationToken.isEmpty())) {
                throw new IllegalStateException("Email verification token is required when requiresVerification is true");
            }

            if ("ACTIVE".equals(statusCode) && verifiedAt == null) {
                throw new IllegalStateException(
                        "User with status 'ACTIVE' must have a verifiedAt timestamp. " +
                                "Please set verifiedAt or use a different status code."
                );
            }

            return new AuthResponseDTO(this);
        }
    }
    // Getters
    public String getStatusCode() {
        return statusCode;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getMessage() {
        return message;
    }

    public SecurityDataResponseDto getSecurityDataResponseDto() {
        return securityDataResponseDto;
    }
    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setSecurityDataResponseDto(SecurityDataResponseDto securityDataResponseDto) {
        this.securityDataResponseDto = securityDataResponseDto;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public void setRequiresVerification(boolean requiresVerification) {
        this.requiresVerification = requiresVerification;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
