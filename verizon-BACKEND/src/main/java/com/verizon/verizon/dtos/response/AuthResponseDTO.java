package com.verizon.verizon.dtos.response;

import com.verizon.verizon.dtos.entities_dto.UserDTO;
import com.verizon.verizon.userstatuses.NonActiveStatus;
import com.verizon.verizon.userstatuses.UserStatus;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public class AuthResponseDTO {
    private final UserDTO userDTO;
    private final String accessToken;
    private final String message;
    private final UserStatus status;
    private final boolean requiresVerification;
    private final String verificationToken;
    private final SecurityDataResponseDto securityDataResponseDto;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastLogin;

   public AuthResponseDTO(Builder builder){
        this.userDTO = builder.userDTO;
        this.accessToken = builder.accessToken;
        this.message = builder.message;
        this.status = builder.status;
        this.requiresVerification = builder.requiresVerification;
        this.verificationToken = builder.verificationToken;
        this.createdAt = builder.createdAt;
        this.lastLogin =  builder.lastLogin;
        this.securityDataResponseDto = builder.securityDataResponseDto;
    }

    public UserDTO getUser() {
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

    @Nullable
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public UserStatus status() {
        return status;
    }

    public boolean isRequiresVerification() {
        return requiresVerification;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    @Nullable
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public static class Builder{
        // required primitive params
        private final String accessToken;
        private final String message;

        // non-primitive dependent required param
        private  UserDTO userDTO;
        private  SecurityDataResponseDto securityDataResponseDto;

        // optional params
        private  UserStatus status = new NonActiveStatus();
        private  boolean requiresVerification =false;
        private  String verificationToken = "";
        @Nullable
        private  LocalDateTime createdAt = LocalDateTime.now();
        @Nullable
        private  LocalDateTime lastLogin = LocalDateTime.now();

        // constructor for required param
        public Builder (String accessToken, String message){
            this.accessToken = accessToken;
            this.message = message;
        }

        // factory methods for optional params
        public Builder status (UserStatus status){
            this.status = status;
            return this;
        }
        public Builder requiresVerification (boolean requiresVerification){
            this.requiresVerification = requiresVerification;
            return this;
        }
        public Builder verificationToken (String verificationToken){
            this.verificationToken = verificationToken;
            return this;
        }
        public Builder createdAt (LocalDateTime createdAt){
            this.createdAt = createdAt;
            return this;
        }
        public Builder lastLogin (LocalDateTime lastLogin){
            this.lastLogin = lastLogin;
            return this;
        }

        // constructor for non-primitive dependent required param
        public Builder userDTO(UserDTO userDTO){
          this.userDTO = userDTO;
          return this;

        }

        public Builder securityDataResponseDto(SecurityDataResponseDto securityDataResponseDto){
            this.securityDataResponseDto = securityDataResponseDto;
            return this;
        }
        public AuthResponseDTO build(){
            return new AuthResponseDTO(this);
        }

    }
}
