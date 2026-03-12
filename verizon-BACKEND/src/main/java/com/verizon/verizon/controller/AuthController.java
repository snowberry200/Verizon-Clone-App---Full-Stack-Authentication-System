package com.verizon.verizon.controller;
import com.verizon.verizon.dtos.request.AuthRequestDTO;
import com.verizon.verizon.dtos.response.AuthResponseDTO;
import com.verizon.verizon.service.AuthServiceImpl;
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
    public ResponseEntity<AuthResponseDTO> signUp(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponseDTO = authServiceImpl.signUp(authRequestDTO); // authResponseDTO
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDTO);  // ResponseEntity
    }

    //SIGN IN
    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> signIn(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponseDTO = authServiceImpl.signIn(authRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);


    }

    // FOR SECURITY CHALLENGE
    @PostMapping("/2fa")
    public ResponseEntity<AuthResponseDTO> forSecurityChallenge(@RequestBody AuthRequestDTO authRequestDTO) {
        AuthResponseDTO authResponseDTO = authServiceImpl.forSecurityChallenge(authRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);

    }

}


