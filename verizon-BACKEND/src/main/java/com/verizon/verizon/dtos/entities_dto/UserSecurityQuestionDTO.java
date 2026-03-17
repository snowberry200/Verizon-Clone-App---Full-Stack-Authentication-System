package com.verizon.verizon.dtos.entities_dto;

import com.verizon.verizon.entity.SecurityQuestion;
import com.verizon.verizon.entity.UserSecurityQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserSecurityQuestionDTO {

    private final Long id;
    private final String answer;
    private SecurityQuestionDTO securityQuestionDTO;
    private List<UserDTO> userDTOS = new ArrayList<>();

    public UserSecurityQuestionDTO(Builder builder) {
        this.id = builder.id;
        this.answer = builder.answer;
        this.securityQuestionDTO = builder.securityQuestionDTO;
        this.userDTOS = builder.userDTOS;
    }


    //method to add single User
    public void addSingleUserDTO(UserDTO userDTO) {
        if (this.userDTOS == null) {
            this.userDTOS = new ArrayList<>();
        }
        if (!this.userDTOS.contains(userDTO)) {
            this.userDTOS.add(userDTO);
            userDTO.setUserSecurityQuestionDTO(this);
        }
    }


    public static class Builder {
        private final Long id;
        private final String answer;
        SecurityQuestionDTO securityQuestionDTO = null;
        private List<UserDTO> userDTOS = new ArrayList<>();

        // constructor for required primitive params
        public Builder(Long id, String answer) {
            this.id = id;
            this.answer = answer;
        }

        //constructor for required non-primitive param
        public Builder securityQuestionDTO(SecurityQuestionDTO securityQuestionDTO) {
            this.securityQuestionDTO = securityQuestionDTO;
            return this;
        }

        //constructor for optional non-primitive param
        public Builder userDTO(List<UserDTO> userDTOS) {
            this.userDTOS = userDTOS;
            return this;
        }

        public UserSecurityQuestionDTO build() {
            return new UserSecurityQuestionDTO(this);
        }
    }


    public List<UserDTO> getUserDTOS() {
        return userDTOS;
    }

    public void setUserDTOS(List<UserDTO> userDTOS) {
        this.userDTOS = userDTOS;
    }

    public SecurityQuestionDTO getSecurityQuestionDTO() {
        return securityQuestionDTO;
    }

    public void setSecurityQuestionDTO(SecurityQuestionDTO securityQuestionDTO) {
        this.securityQuestionDTO = securityQuestionDTO;
    }

    public Long getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    //convert UserSecurityQuestion to UserSecurityQuestionDTO
    public static UserSecurityQuestionDTO convertUserSecurityQuestionDTO(UserSecurityQuestion userSecurityQuestion) {
        if (userSecurityQuestion == null) {
            return null;
        }
        UserSecurityQuestionDTO newUserSecurityQuestionDTO = new UserSecurityQuestionDTO.Builder(userSecurityQuestion.getId(), userSecurityQuestion.getAnswer())
                .build();
        // .securityQuestion(userSecurityQuestion.getQuestion())
        if (userSecurityQuestion.getQuestion() != null) {
            SecurityQuestion securityQuestion = userSecurityQuestion.getQuestion();
            SecurityQuestionDTO newSecurityQuestionDTO = SecurityQuestionDTO.convertSecurityQuestionToDTO(securityQuestion);
            newUserSecurityQuestionDTO.setSecurityQuestionDTO(newSecurityQuestionDTO);
        }
        return newUserSecurityQuestionDTO;
    }
}
