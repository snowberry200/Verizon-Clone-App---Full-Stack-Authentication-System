package com.verizon.verizon.controller;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.verizon.verizon.InvalidCredentialException;
import com.verizon.verizon.dtos.request.AuthRequestDTO;
import com.verizon.verizon.dtos.response.AuthResponseDTO;
import com.verizon.verizon.exceptions.AccountNotActiveException;
import com.verizon.verizon.exceptions.EmailAlreadyExistsException;
import com.verizon.verizon.exceptions.InvalidTokenException;
import com.verizon.verizon.service.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    //REGISTRATION
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> signUp(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponseDTO = authServiceImpl.signUp(authRequestDTO); // authResponseDTO
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDTO);  // ResponseEntity
    }


    //SIGN IN
    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> signIn(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponseDTO = authServiceImpl.signIn(authRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);


    }

    // FOR SECURITY CHALLENGE
    @PostMapping("/2fa")
    public ResponseEntity<AuthResponseDTO> forSecurityChallenge(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponseDTO = authServiceImpl.forSecurityChallenge(authRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);

    }

    // EMAIL VERIFICATION
    @GetMapping("/verify")
    public ResponseEntity<AuthResponseDTO> verifyEmail(@RequestParam String token) {

        AuthResponseDTO authResponseDTO = authServiceImpl.verifyEmail(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authResponseDTO);
    }

    // RESEND VERIFICATION EMAIL
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationEmail(@RequestParam String email) {

        authServiceImpl.resendVerificationEmail(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Verification email sent successfully");
    }

    // Exception handlers
    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<String> handleAccountNotActive(AccountNotActiveException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpired(TokenExpiredException ex) {
        return ResponseEntity
                .status(HttpStatus.GONE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }
}


