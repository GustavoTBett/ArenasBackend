package com.projetoWeb.Arenas.service;

import com.projetoWeb.Arenas.model.RefreshToken;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private long accessTokenExpirationMinutes = 15;

    private long refreshTokenExpirationDays = 7;

    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities) {
        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer("arenas_backend")
                .subject(email)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES))
                .claim("scope", authorities)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public ResponseCookie generateResponseCookieLogin(String jwt) {
        return ResponseCookie.from("jwt-token", jwt)
                .httpOnly(true)       // Impede acesso via JavaScript
                .secure(true)         // Enviar apenas em HTTPS (essencial em produção)
                .path("/")            // Válido para todas as rotas da aplicação
                .maxAge(accessTokenExpirationMinutes * 60)    // Tempo de vida do cookie (em segundos)
                .sameSite("Lax")      // Proteção contra CSRF. 'Strict' é mais seguro, mas 'Lax' é um bom padrão para SPAs.
                .build();
    }

    public ResponseCookie generateResponseCookieLogout() {
        return ResponseCookie.from("jwt-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // Instruí o navegador a apagar o cookie imediatamente
                .sameSite("Lax")
                .build();
    }

    public void invalidateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    public String generateAndSaveRefreshToken(String email) {
        User user = userService.getUserByEmail(email);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    /**
     * Cria o cookie para o Refresh Token.
     * Note o path mais restrito para maior segurança.
     */
    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from("refresh-token", token)
                .httpOnly(true)
                .secure(true)
                .path("/user/refresh-token") // Só será enviado para o endpoint de refresh
                .maxAge(refreshTokenExpirationDays * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie generateResponseRefreshTokeCookieLogout() {
        return ResponseCookie.from("refresh-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/user/refresh-token")
                .maxAge(0) // Instruí o navegador a apagar o cookie imediatamente
                .sameSite("Strict")
                .build();
    }

    /**
     * Valida um Refresh Token.
     * Retorna o email do usuário se o token for válido.
     */
    public Optional<String> validateRefreshToken(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);

        if (refreshTokenOpt.isEmpty()) {
            return Optional.empty(); // Token não encontrado
        }

        RefreshToken refreshToken = refreshTokenOpt.get();
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            return Optional.empty(); // Token expirado
        }

        return Optional.of(refreshToken.getUser().getEmail());
    }
}
