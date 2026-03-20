package com.verizon.verizon.dtos.request;


import com.verizon.verizon.dtos.entities_dto.RolesDTO;
import com.verizon.verizon.dtos.entities_dto.UserDTO;
import com.verizon.verizon.dtos.entities_dto.UserSecurityQuestionDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public class AuthRequestDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private final String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private final String password;

    @NotNull(message = "Security data is required")
    private final SecurityDataRequestDTO securityDataRequestDTO;

    @NotBlank(message = "Name is required")
    private final String name;


    private final String statusCode;

    AuthRequestDTO(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.name = builder.name;
        this.statusCode = builder.statusCode;
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

    public String getStatusCode() {
        return statusCode;
    }

    public SecurityDataRequestDTO getSecurityDataRequestDTO() {
        return securityDataRequestDTO;
    }


    public static class Builder {
        //required primitive params
        private final String email;
        private final String password;
        private final String name;
        //required non-primitive dependent param
        private SecurityDataRequestDTO securityDataRequestDTO;
        //optional primitive param
        private String statusCode ;

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
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name is required");
            }

            this.email = email;
            this.password = password;
            this.name = name;
            // Set defaults for optional fields
            this.statusCode = "NONACTIVE";  // Or whatever your default is
            this.securityDataRequestDTO = null;
        }


        //constructor for required non-primitive dependent params
        public Builder securityDataRequestDTO(SecurityDataRequestDTO securityDataRequestDTO) {
            this.securityDataRequestDTO = securityDataRequestDTO;
            return this;
        }

        public Builder statusCode(String statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public AuthRequestDTO build() {
            return new AuthRequestDTO(this);

        }

    }

}



