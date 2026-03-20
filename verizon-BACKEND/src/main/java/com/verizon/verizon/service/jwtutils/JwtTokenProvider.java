package com.verizon.verizon.service.jwtutils;

import com.verizon.verizon.entity.Roles;
import com.verizon.verizon.entity.User;
import java.security.Key;

public interface JwtTokenProvider{
    // a. create signing key
     Key createSignInKey();
     // b. create Token
    String createAccessToken(User user);
    // c. validate Token
    boolean validateToken(String token);
    // d. extract userName from Token
    String extractEmail(String token);
    // e. extract id
    Long extractId(String token);
    // f. extract Role from Token
    Roles extractRoleFromToken(String token);
    // g. verification Token
    String createVerificationToken(User user);
}