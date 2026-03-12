package com.verizon.verizon.service;

import com.verizon.verizon.dtos.request.AuthRequestDTO;
import com.verizon.verizon.dtos.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO signUp(AuthRequestDTO authRequestDTO);
    AuthResponseDTO signIn(AuthRequestDTO authRequestDTO);
    AuthResponseDTO forSecurityChallenge(AuthRequestDTO authRequestDTO);
}
