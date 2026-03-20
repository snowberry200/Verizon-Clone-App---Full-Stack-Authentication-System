package com.verizon.verizon.service.jwtutils;
import com.verizon.verizon.entity.Roles;
import com.verizon.verizon.entity.User;
import com.verizon.verizon.repository.RoleRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider{
    @Value("${jwt.secret:yourSuperSecretKeyThatIsAtLeast32CharactersLong}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // 24 hours in milliseconds
    private long jwtExpiration;

    RoleRepository roleRepository;

    JwtTokenProviderImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public Key createSignInKey() {
        byte[] keyBytes = jwtSecret.getBytes();  // Convert string to bytes
        return Keys.hmacShaKeyFor(keyBytes);     // Create HMAC SHA key
    }


    @Override
    public String createAccessToken(User user) {
        try {
            Map<String,Object> claims = new HashMap<>();
            claims.put("email", user.getEmail());
            claims.put("userId", user.getId());
            claims.put("roles", user.getRoles().stream().findFirst().map(Roles::getName).orElse("USER"));
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                    .signWith(createSignInKey())
                    .compact();
        } catch (InvalidKeyException e) {
            throw new RuntimeException("InvalidKeyException: " + e);
        }
    }

    @Override
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(createSignInKey())
                    .build()
                    .parseClaimsJws(token);
        return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(createSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    @Override
    public Long extractId(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(createSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userId", Long.class);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Roles extractRoleFromToken(String token) {
        try {
             String roleName = Jwts.parserBuilder()
                    .setSigningKey(createSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("roles", String.class);

            return roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createVerificationToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("type", "VERIFICATION_TOKEN")  // Different type!
                .claim("purpose", "EMAIL_VERIFICATION")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(createSignInKey())
                .compact();
    }
}