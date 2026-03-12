//package com.verizon.verizon.service;
//
//import com.verizon.verizon.dtos.entities_dto.UserDTO;
//import com.verizon.verizon.dtos.request.AuthRequestDTO;
//import com.verizon.verizon.dtos.response.AuthResponseDTO;
//import com.verizon.verizon.entity.User;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SecurityDataService {
//    private AuthRepository userRepository;  // Changed from SecurityQuestionRepository
//
//    public AuthResponseDTO verifyOwnership(AuthRequestDTO authRequestDTO) {
//        String email = authRequestDTO.getEmail();
//        User user = userRepository.findByEmail(email)  // Now this works!
//                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//
//        UserDTO userDTO = UserDTO.convertToUserDTO(user);
//        String securityQuestion = authRequestDTO.getSecurityQuestion();
//        String securityAnswer = authRequestDTO.getSecurityAnswer();
//
//        return AuthResponseDTO.verifyOwnership(
//                null,
//                "please your question and answer",
//                userDTO,
//                securityAnswer,
//                securityQuestion,
//                true
//        );
//    }
//}