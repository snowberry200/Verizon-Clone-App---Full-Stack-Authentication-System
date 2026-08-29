package com.verizon.verizon.dtos.entities_dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.verizon.verizon.entity.Roles;
import java.util.ArrayList;
import java.util.List;

public class RolesDTO {
    private final Long id;
    private final String name;

    @JsonIgnore
    private List<UserDTO> userDTOS;

    private RolesDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.userDTOS = builder.userDTOS;
    }

    public static RolesDTO convertRolesToDTO(Roles roles) {
        if (roles == null) {
            return null;
        }

        // Note: Not adding users to avoid circular reference
        // Users can be accessed through UserDTO if needed

        return new Builder(roles.getName(), roles.getId())
                .build();
    }

    public void addSingleUserDTO(UserDTO userDTO) {
        if (this.userDTOS == null) {
            this.userDTOS = new ArrayList<>();
        }
        if (!this.userDTOS.contains(userDTO)) {
            this.userDTOS.add(userDTO);
            // Back-reference removed to prevent circular reference
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public List<UserDTO> getUserDTOS() { return userDTOS; }
    public void setUserDTOS(List<UserDTO> userDTOS) { this.userDTOS = userDTOS; }

    public static class Builder {
        private final String name;
        private Long id;
        private List<UserDTO> userDTOS;

        public Builder(String name, Long id) {
            this.name = name;
            this.id = id;
        }

        public Builder userDTO(List<UserDTO> userDTOS) {
            this.userDTOS = userDTOS;
            return this;
        }

        public RolesDTO build() {
            return new RolesDTO(this);
        }
    }
}