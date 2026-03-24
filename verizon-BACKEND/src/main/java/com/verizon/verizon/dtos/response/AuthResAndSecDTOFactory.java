package com.verizon.verizon.dtos.response;
import com.verizon.verizon.constant.Validator;
import com.verizon.verizon.dtos.entities_dto.UserDTO;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class AuthResAndSecDTOFactory {

    private AuthResAndSecDTOFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // SIGN IN - One clear method
    public static AuthResponseDTO forSignIn(
            String accessToken,
            String message,
            UserDTO userDTO,
            LocalDateTime lastLogin,
            LocalDateTime verifiedAt
    ) {
        Objects.requireNonNull(accessToken, Validator.ACCESS_TOKEN_NON_NULL);
        Objects.requireNonNull(message, Validator.MESSAGE_NON_NULL);
        Objects.requireNonNull(userDTO, Validator.USERDTO_NON_NULL);

        return new AuthResponseDTO.Builder(message)
                .accessToken(accessToken)  // ✅ Add accessToken
                .userDTO(userDTO)
                .statusCode(Validator.ACTIVE)
                .requiresVerification(false)
                .lastLogin(lastLogin != null ? lastLogin : LocalDateTime.now())
                .verifiedAt(verifiedAt != null ? verifiedAt : LocalDateTime.now())
                .build();
    }

    // REGISTRATION - One clear method
    public static AuthResponseDTO forSignUp(
            String message,
            UserDTO userDTO,
            SecurityDataResponseDto securityDataResponseDto
    ) {
        Objects.requireNonNull(message, Validator.MESSAGE_NON_NULL);
        Objects.requireNonNull(userDTO, Validator.USERDTO_NON_NULL);
        Objects.requireNonNull(securityDataResponseDto, Validator.SECURITY_DATA_NON_NULL);

        // Generate verification token
        String emailVerificationToken = UUID.randomUUID().toString();

        return new AuthResponseDTO.Builder(message)
                .userDTO(userDTO)
                .securityDataResponseDto(securityDataResponseDto)
                .statusCode(Validator.NONACTIVE)
                .requiresVerification(true)
                .emailVerificationToken(emailVerificationToken)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // EMAIL VERIFICATION - After user clicks link
    public static AuthResponseDTO forEmailVerified(
            String accessToken,
            String message,
            UserDTO userDTO,
            SecurityDataResponseDto securityDataResponseDto
    ) {
        Objects.requireNonNull(accessToken, Validator.ACCESS_TOKEN_NON_NULL);
        Objects.requireNonNull(message, Validator.MESSAGE_NON_NULL);
        Objects.requireNonNull(userDTO, Validator.USERDTO_NON_NULL);
        Objects.requireNonNull(securityDataResponseDto, Validator.SECURITY_DATA_NON_NULL);

        return new AuthResponseDTO.Builder(message)
                .accessToken(accessToken)  // ✅ Add accessToken
                .userDTO(userDTO)
                .securityDataResponseDto(securityDataResponseDto)
                .statusCode(Validator.ACTIVE)
                .requiresVerification(false)
                .verifiedAt(LocalDateTime.now())  // ✅ Add verified timestamp
                .lastLogin(LocalDateTime.now())
                .build();
    }

    // SECURITY CHALLENGE - One clear method
    public static AuthResponseDTO forSecurityChallenge(
            String accessToken,
            String message,
            UserDTO userDTO,
            SecurityDataResponseDto securityDataResponseDto,
            LocalDateTime lastLogin
    ) {
        Objects.requireNonNull(accessToken, Validator.ACCESS_TOKEN_NON_NULL);
        Objects.requireNonNull(message, Validator.MESSAGE_NON_NULL);
        Objects.requireNonNull(userDTO, Validator.USERDTO_NON_NULL);
        Objects.requireNonNull(securityDataResponseDto, Validator.SECURITY_DATA_NON_NULL);

        return new AuthResponseDTO.Builder(message)
                .accessToken(accessToken)  // Add accessToken
                .userDTO(userDTO)
                .securityDataResponseDto(securityDataResponseDto)
                .statusCode(Validator.ACTIVE)
                .requiresVerification(false)
                .lastLogin(lastLogin != null ? lastLogin : LocalDateTime.now())
                .build();
    }

    // ERROR - One clear method
    public static AuthResponseDTO forError(String message) {
        Objects.requireNonNull(message, Validator.MESSAGE_NON_NULL);

        return new AuthResponseDTO.Builder(message)
                .statusCode("ERROR")
                .requiresVerification(false)
                .build();
    }

    // Factory method for SecurityDataResponseDto
    public static SecurityDataResponseDto createSecurityDataResponseDto(
            String securityQuestion,
            String securityAnswer
    ) {
        Objects.requireNonNull(securityQuestion, Validator.SECURITY_QUESTION_NON_NULL);
        Objects.requireNonNull(securityAnswer, Validator.SECURITY_ANSWER_NON_NULL);

        return new SecurityDataResponseDto.Builder(securityQuestion, securityAnswer)
                .build();
    }
}