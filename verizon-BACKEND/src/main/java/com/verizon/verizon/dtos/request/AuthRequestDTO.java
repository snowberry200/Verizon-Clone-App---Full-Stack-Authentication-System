package com.verizon.verizon.dtos.request;

import com.verizon.verizon.userstatuses.NonActiveStatus;
import com.verizon.verizon.userstatuses.UserStatus;

public class AuthRequestDTO {
    private final String email;
    private final String password;
    private final SecurityDataRequestDTO securityDataRequestDTO;
    private final String name;
    private final UserStatus status;

    AuthRequestDTO(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.name = builder.name;
        this.status = builder.status;
        this.securityDataRequestDTO = builder.securityDataRequestDTO;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public SecurityDataRequestDTO getSecurityDataRequestDTO() {
        return securityDataRequestDTO;
    }

    public UserStatus status() {
        return status;
    }


    public static class Builder {
        //required primitive params
        private final String email;
        private final String password;
        private final String name;
        //required non-primitive dependent param
        private SecurityDataRequestDTO securityDataRequestDTO;
        //optional primitive param
        private UserStatus status = new NonActiveStatus();

        //constructor for required primitive params
        public Builder(String email,
                       String password,
                       String name) {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");

            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }
            this.email = email;
            this.password = password;
            this.name = name;
        }

        //constructor for optional primitive param
        public Builder status(UserStatus status) {
            this.status = status;
            return this;
        }

        //constructor for required non-primitive dependent params
        public Builder securityDataRequestDTO(SecurityDataRequestDTO securityDataRequestDTO) {
            this.securityDataRequestDTO = securityDataRequestDTO;
            return this;
        }

        public AuthRequestDTO build() {
            return new AuthRequestDTO(this);

        }

    }

}
