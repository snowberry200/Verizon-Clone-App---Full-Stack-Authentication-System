package com.verizon.verizon.service;

import com.verizon.verizon.InvalidCredentialException;
import com.verizon.verizon.dtos.entities_dto.UserDTO;
import com.verizon.verizon.dtos.request.AuthRequestDTO;
import com.verizon.verizon.dtos.response.AuthResAndSecDTOFactory;
import com.verizon.verizon.dtos.response.AuthResponseDTO;
import com.verizon.verizon.dtos.response.SecurityDataResponseDto;
import com.verizon.verizon.entity.*;
import com.verizon.verizon.repository.RoleRepository;
import com.verizon.verizon.repository.SecurityQuestionRepository;
import com.verizon.verizon.repository.UserRepository;
import com.verizon.verizon.repository.UserSecurityQuestionRepository;
import com.verizon.verizon.service.jwtutils.JwtTokenProviderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecurityQuestionRepository securityQuestionRepository;
    private final UserSecurityQuestionRepository userSecurityQuestionRepository;
    private final JwtTokenProviderImpl jwtTokenProviderImpl;

    public AuthServiceImpl(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            RoleRepository roleRepository,
            SecurityQuestionRepository securityQuestionRepository,
            UserSecurityQuestionRepository userSecurityQuestionRepository,
            JwtTokenProviderImpl jwtTokenProviderImpl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.securityQuestionRepository = securityQuestionRepository;
        this.userSecurityQuestionRepository = userSecurityQuestionRepository;
        this.jwtTokenProviderImpl = jwtTokenProviderImpl;
    }

    // REGISTRATION
    @Override
    public AuthResponseDTO signUp(AuthRequestDTO authRequestDTO) {
        //CHECK IF EMAIL EXIST IN DB
        boolean emailExists = userRepository.existsByEmail(authRequestDTO.getEmail());
        if (emailExists) {
            throw new RuntimeException("email already exists");
        }
        //hash password
        String hashedPassword = passwordEncoder.encode(authRequestDTO.getPassword());

        // GET SECURITY QUESTION
        SecurityQuestion securityQuestion = securityQuestionRepository
                .findByQuestionText(authRequestDTO.getSecurityDataRequestDTO().getSecurityQuestion())
                .orElseThrow(() -> new RuntimeException("please choose a security question"));

        // CREATE USER SECURITY QUESTION
        UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion
                .Builder(authRequestDTO.getSecurityDataRequestDTO().getSecurityAnswer())
                .question(securityQuestion)
                .build();

        // Save UserSecurityQuestion FIRST
        UserSecurityQuestion savedUserSecurityQuestion = userSecurityQuestionRepository.save(userSecurityQuestion);

        // GET DEFAULT ROLE FROM DB
        Roles defaultRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Roles newRole = new Roles.Builder("USER").build();
                    return roleRepository.save(newRole);
                });

        // CREATE USER
        User user = new User.Builder(authRequestDTO.getName(), authRequestDTO.getEmail(), hashedPassword)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .userSecurityQuestion(savedUserSecurityQuestion)
                .roles(List.of(defaultRole))
                .build();

        // Establish bidirectional relationships
        defaultRole.addSingleUserInRole(user);
        savedUserSecurityQuestion.addSingleUser(user);

        // Save user to database
        User savedUser = userRepository.save(user);

        // CREATE ACCESS-TOKEN
        String accessToken = jwtTokenProviderImpl.createToken(savedUser);
        String message = "Registration successful, please Login to your new account";

        // Create SecurityDataResponseDto
        SecurityDataResponseDto securityDataResponseDto = AuthResAndSecDTOFactory.createSecurityDataResponseDto(
                securityQuestion.getQuestionText(),
                "Security answer has been saved successfully"
        );

        return new AuthResponseDTO.Builder(accessToken, message)
                .isActive(true)
                .requiresVerification(false)
                .securityDataResponseDto(securityDataResponseDto)
                .build();
    }

    // SIGN IN
    @Override
    public AuthResponseDTO signIn(AuthRequestDTO authRequestDTO) {
        String message = "Welcome to Verizon";

        // Find user
        User user = userRepository.findByEmail(authRequestDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialException("Invalid credentials"));

        // Verify password
        boolean validatePassword = passwordEncoder.matches(
                authRequestDTO.getPassword(),
                user.getPassword()
        );

        if (!validatePassword) {
            throw new InvalidCredentialException("Invalid credentials");
        }

        // Verify if user has security question
        boolean userHasUserSecurityQuestion = userSecurityQuestionRepository
                .existsByUsersId(user.getId());

        if (!userHasUserSecurityQuestion) {
            throw new InvalidCredentialException("Registration incomplete");
        }

        // Generate access token
        String tempToken = jwtTokenProviderImpl.createToken(user);

        // Build response
        return new AuthResponseDTO.Builder(tempToken, message)
                .userDTO(UserDTO.convertToUserDTO(user))
                .lastLogin(LocalDateTime.now())
                .build();
    }

    // TWO FACTOR SECURITY
    @Override
    public AuthResponseDTO forSecurityChallenge(AuthRequestDTO request) {
        // FIXED: Use 'request' parameter, not undefined 'authRequestDTO'
        String email = request.getEmail();

        // Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get user security question
        UserSecurityQuestion userSecurityQuestion = userSecurityQuestionRepository
                .findByUsersId(user.getId())
                .orElseThrow(() -> new InvalidCredentialException("Security question required"));

        // Verify answer
        String answer = request.getSecurityDataRequestDTO().getSecurityAnswer();
        if (!userSecurityQuestion.getAnswer().equals(answer)) {
            throw new InvalidCredentialException("Invalid security answer");
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate token
        String token = jwtTokenProviderImpl.createToken(user);
        String message = "Welcome to Verizon";

        // Create security response DTO
        String securityQuestionText = userSecurityQuestion.getQuestion().getQuestionText();
        SecurityDataResponseDto securityDataResponseDto =
                new SecurityDataResponseDto.Builder(
                        securityQuestionText,
                        "Security answer verified successfully"
                ).build();

        return new AuthResponseDTO.Builder(token, message)
                .userDTO(UserDTO.convertToUserDTO(user))
                .securityDataResponseDto(securityDataResponseDto)
                .build();
    }
}