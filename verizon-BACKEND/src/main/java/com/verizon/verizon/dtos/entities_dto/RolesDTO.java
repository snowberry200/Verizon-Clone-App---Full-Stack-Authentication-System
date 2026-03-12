package com.verizon.verizon.dtos.entities_dto;

import com.verizon.verizon.entity.Roles;
import com.verizon.verizon.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RolesDTO {
    private final Long id;
    private final String name;
    private List<UserDTO> userDTOS;

    private RolesDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.userDTOS = builder.userDTOS;
    }
        //convert roles to rolesDTO
    public static RolesDTO convertRolesToDTO(Roles roles) {
        if (roles == null) {
            return null;  // Null safety
        }
      RolesDTO newRolesDTO =  new RolesDTO.Builder(roles.getName(),roles.getId())
                //.userDTO(roles.getUsers())
                .build();
        if(roles.getUsers()!=null && !roles.getUsers().isEmpty()){
           List <User> newUser = roles.getUsers();
           newUser.stream().map(user -> {
               UserDTO newUserDTO = UserDTO.convertToUserDTO(user);
               newRolesDTO.addSingleUserDTO(newUserDTO);//<- maintaining bi-directional relation
               newUserDTO.addSingleRoleDTO(newRolesDTO);
               return newUserDTO;

           }).collect(Collectors.toList());
        }
        return newRolesDTO;
    }

    // add single UserDTO
    public void addSingleUserDTO(UserDTO userDTO) {
        if (this.userDTOS == null) {
            this.userDTOS = new ArrayList<>();
        }
        if (!this.userDTOS.contains(userDTO)) {
            this.userDTOS.add(userDTO);
            userDTO.addSingleRoleDTO(this); // ← Maintain the OTHER side!
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<UserDTO> getUserDTOS() {
        return userDTOS;
    }

    public void setUserDTOS(List<UserDTO> userDTOS) {
        this.userDTOS = userDTOS;
    }

    public static class Builder {
        private final String name;
        private Long id;
        private List<UserDTO> userDTOS;

        // constructor for required params
        public Builder(String name, Long id) {
            this.name = name;
            this.id = id;
        }

        // constructor for non-primitive param
        public Builder userDTO(List<UserDTO> userDTOS) {
            this.userDTOS = userDTOS;
            return this;
        }


        public RolesDTO build() {
            return new RolesDTO(this);
        }
    }

}
