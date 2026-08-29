package com.verizon.verizon.service;

import com.verizon.verizon.InvalidCredentialException;
import com.verizon.verizon.constant.Validator;
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
import jakarta.transaction.Transactional;
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

    // Helper method to get or create verifiedAt timestamp
    private LocalDateTime getVerifiedAt(User user) {
        LocalDateTime existingVerifiedAt = user.getVerifiedAt();
        if (existingVerifiedAt != null) {
            return existingVerifiedAt;
        }

        if (Validator.ACTIVE.equals(user.getStatusCode())) {
            LocalDateTime newVerifiedAt = LocalDateTime.now();
            user.setVerifiedAt(newVerifiedAt);
            userRepository.save(user);
            return newVerifiedAt;
        }

        return null;
    }

    // REGISTRATION
    @Override
    public AuthResponseDTO signUp(AuthRequestDTO authRequestDTO) {
        // CHECK IF EMAIL EXISTS IN DB
        boolean emailExists = userRepository.existsByEmail(authRequestDTO.getEmail());
        if (emailExists) {
            throw new InvalidCredentialException("Email already exists");
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(authRequestDTO.getPassword());

        // GET SECURITY QUESTION
        SecurityQuestion securityQuestionName = securityQuestionRepository.findByName(authRequestDTO.getSecurityDataRequestDTO().getSecurityQuestion())
                //.findByQuestionText(authRequestDTO.getSecurityDataRequestDTO().getSecurityQuestion())
                .orElseThrow(() -> new RuntimeException("Please choose a security question"));

        // CREATE USER SECURITY QUESTION
        UserSecurityQuestion userSecurityQuestion = new UserSecurityQuestion
                .Builder(authRequestDTO.getSecurityDataRequestDTO().getSecurityAnswer())
                .question(securityQuestionName)
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
                .statusCode(Validator.NONACTIVE)
                .userSecurityQuestion(savedUserSecurityQuestion)
                .roles(List.of(defaultRole))
                .build();

        // Generate verification token
        String emailVerificationToken = jwtTokenProviderImpl.createVerificationToken(user);

        // Set token on user
        user.setVerificationToken(emailVerificationToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));

        // Establish bidirectional relationships
        defaultRole.addSingleUserInRole(user);
        savedUserSecurityQuestion.addSingleUser(user);

        // Save user to database
        User savedUser = userRepository.save(user);

        String message = "Registration successful, please Login to your new account";

        // Create SecurityDataResponseDto
        SecurityDataResponseDto securityDataResponseDto = AuthResAndSecDTOFactory.createSecurityDataResponseDto(
                securityQuestionName.getQuestionText(),
                "Security answer has been saved successfully"
        );

        return new AuthResponseDTO.Builder(message)
                .statusCode(Validator.NONACTIVE)
                .requiresVerification(true)
                .userDTO(UserDTO.convertToUserDTO(savedUser))
                .emailVerificationToken(emailVerificationToken)
                .createdAt(savedUser.getCreatedAt())
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

        // Check if user is ACTIVE
        if (!Validator.ACTIVE.equals(user.getStatusCode())) {
            throw new InvalidCredentialException("Account not verified. Please verify your email first.");
        }

        // Get or create verifiedAt using helper method
        LocalDateTime verifiedAt = getVerifiedAt(user);

        // Create SecurityDataResponseDto
        SecurityDataResponseDto securityDataResponseDto = AuthResAndSecDTOFactory.createSecurityDataResponseDto(
                user.getUserSecurityQuestion().getQuestion().getQuestionText(),
                "Security question verified"
        );

        // Generate access token
        String tempToken = jwtTokenProviderImpl.createAccessToken(user);

        // Build response
        return new AuthResponseDTO.Builder(message)
                .statusCode(Validator.ACTIVE)
                .userDTO(UserDTO.convertToUserDTO(user))
                .accessToken(tempToken)
                .securityDataResponseDto(securityDataResponseDto)
                .lastLogin(LocalDateTime.now())
                .verifiedAt(verifiedAt)
                .build();
    }

    // TWO FACTOR SECURITY
    @Override
    public AuthResponseDTO forSecurityChallenge(AuthRequestDTO request) {
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

        // Get or create verifiedAt using helper method
        LocalDateTime verifiedAt = getVerifiedAt(user);

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generate token
        String accessToken = jwtTokenProviderImpl.createAccessToken(user);
        String message = "Welcome to Verizon";

        // Update last login
        LocalDateTime now = LocalDateTime.now();


        // Create security response DTO
        String securityQuestionText = userSecurityQuestion.getQuestion().getQuestionText();
        SecurityDataResponseDto securityDataResponseDto =
                new SecurityDataResponseDto.Builder(
                        securityQuestionText,
                        "Security answer verified successfully"
                ).build();

        return new AuthResponseDTO.Builder(message)
                .userDTO(UserDTO.convertToUserDTO(user))
                .securityDataResponseDto(securityDataResponseDto)
                .statusCode(Validator.ACTIVE)
                .accessToken(accessToken)
                .verifiedAt(verifiedAt)
                .lastLogin(now)
                .build();
    }

    // EMAIL VERIFICATION
    @Override
    @Transactional
    public AuthResponseDTO verifyEmail(String token) {
        // Validate token and get user
        String email = jwtTokenProviderImpl.validateVerificationToken(token);

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialException("Invalid verification token"));

        // Check if already verified
        if (Validator.ACTIVE.equals(user.getStatusCode())) {
            throw new InvalidCredentialException("Account already verified");
        }

        // Check token expiry
        if (user.getVerificationTokenExpiry() != null &&
                user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialException("Verification token has expired");
        }

        // Activate user
        user.setStatusCode(Validator.ACTIVE);
        user.setVerifiedAt(LocalDateTime.now());
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);

        User savedUser = userRepository.save(user);

        // Generate access token (NOW they can log in!)
        String accessToken = jwtTokenProviderImpl.createAccessToken(savedUser);

        // Create SecurityDataResponseDto
        SecurityDataResponseDto securityDataResponseDto = AuthResAndSecDTOFactory.createSecurityDataResponseDto(
                savedUser.getUserSecurityQuestion().getQuestion().getQuestionText(),
                "Email verified successfully"
        );

        // Return response WITH access token
        return new AuthResponseDTO.Builder("Email verified successfully! You can now log in.")
                .accessToken(accessToken)
                .userDTO(UserDTO.convertToUserDTO(savedUser))
                .securityDataResponseDto(securityDataResponseDto)
                .statusCode(Validator.ACTIVE)
                .requiresVerification(false)
                .verifiedAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();
    }

    // RESEND VERIFICATION EMAIL
    @Override
    @Transactional
    public AuthResponseDTO resendVerificationEmail(String email) {
        // Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialException("User not found with email: " + email));

        // Check if already verified
        if (Validator.ACTIVE.equals(user.getStatusCode())) {
            throw new InvalidCredentialException("Account already verified");
        }

        // Generate new verification token
        String newToken = jwtTokenProviderImpl.createVerificationToken(user);
        user.setVerificationToken(newToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        // Return response with new token
        return new AuthResponseDTO.Builder("Verification email sent successfully!")
                .userDTO(UserDTO.convertToUserDTO(user))
                .statusCode(Validator.NONACTIVE)
                .requiresVerification(true)
                .emailVerificationToken(newToken)
                .build();
    }
}