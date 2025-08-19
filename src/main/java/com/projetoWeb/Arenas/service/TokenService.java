package com.projetoWeb.Arenas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    public static final Long expiresIn = 3600l;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Value(value = "SPRING_JPA_SHOW_SQL")
    private String value;

    public String generateToken(String email) {
        var now = Instant.now();

        String scopes = "ROLE_USER";

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(email)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        String jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return jwtValue;
    }

    public ResponseCookie generateResponseCookieLogin(String jwt) {
        return ResponseCookie.from("jwt-token", jwt)
                .httpOnly(true)       // Impede acesso via JavaScript
                .secure(!(value.equals("true")))         // Enviar apenas em HTTPS (essencial em produção)
                .path("/")            // Válido para todas as rotas da aplicação
                .maxAge(TokenService.expiresIn)    // Tempo de vida do cookie (em segundos)
                .sameSite("Lax")      // Proteção contra CSRF. 'Strict' é mais seguro, mas 'Lax' é um bom padrão para SPAs.
                .build();
    }

    public ResponseCookie generateResponseCookieLogout() {
        return ResponseCookie.from("jwt-token", "")
                .httpOnly(true)
                .secure(!(value.equals("true")))
                .path("/")
                .maxAge(0) // Instruí o navegador a apagar o cookie imediatamente
                .sameSite("Lax")
                .build();
    }
}
