package com.verizon.verizon.dtos.response;

import com.verizon.verizon.dtos.entities_dto.UserDTO;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuthResponseDTO {
    private final UserDTO userDTO;
    private final String accessToken;
    private final String message;
    private final String statusCode;
    private final boolean requiresVerification;
    private final String verificationToken;
    private final SecurityDataResponseDto securityDataResponseDto;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastLogin;


    private final LocalDateTime verifiedAt;

   public AuthResponseDTO(Builder builder){
        this.userDTO = builder.userDTO;
        this.accessToken = builder.accessToken;
        this.message = builder.message;
        this.statusCode = builder.statusCode;
        this.requiresVerification = builder.requiresVerification;
        this.verificationToken = builder.verificationToken;
        this.createdAt = builder.createdAt;
        this.lastLogin =  builder.lastLogin;
        this.securityDataResponseDto = builder.securityDataResponseDto;
        this.verifiedAt = builder.verifiedAt;
    }


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
        private  String statusCode;
        private  boolean requiresVerification ;
        private  String verificationToken;
        private LocalDateTime verifiedAt;
        @Nullable
        private  LocalDateTime createdAt ;
        @Nullable
        private  LocalDateTime lastLogin;

        // constructor
        public Builder (String accessToken, String message){
            // FOR REQUIRED PARAMS
            // Validate required primitives
            this.accessToken = Objects.requireNonNull(accessToken, "Access token cannot be null");
            this.message = Objects.requireNonNull(message, "Message cannot be null");

            // FOR OPTIONAL PRIMITIVE PARAMS
            this.statusCode = "NONACTIVE";
            this.requiresVerification = false;
            this.verificationToken = "";
            this.createdAt = LocalDateTime.now();
            this.lastLogin = LocalDateTime.now();

            //non-primitive dependent required param - NOT FINAL (set via builder methods).
            // Thus :  userDTO and  securityDataResponseDto
        }

        // factory methods for optional params
        public Builder statusCode (String statusCode){
            this.statusCode = statusCode;
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
        public Builder verifiedAt(LocalDateTime verifiedAt) {
            this.verifiedAt = verifiedAt;
            return this;
        }
        public Builder lastLogin (LocalDateTime lastLogin){
            this.lastLogin = lastLogin;
            return this;
        }

        // method chain for non-primitive dependent required param
        public Builder userDTO(UserDTO userDTO){
          this.userDTO = userDTO;
          return this;

        }

        public Builder securityDataResponseDto(SecurityDataResponseDto securityDataResponseDto){
            this.securityDataResponseDto = securityDataResponseDto;
            return this;
        }
        public AuthResponseDTO build(){
            // VALIDATE required non-primitive fields were set
            if (userDTO == null) {
                throw new IllegalStateException("UserDTO is required but was never set");
            }
            if (securityDataResponseDto == null) {
                throw new IllegalStateException("SecurityDataResponseDto is required but was never set");
            }
            return new AuthResponseDTO(this);
        }

    }
}
