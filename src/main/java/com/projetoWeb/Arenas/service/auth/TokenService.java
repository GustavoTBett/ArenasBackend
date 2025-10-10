package com.projetoWeb.Arenas.service.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.projetoWeb.Arenas.model.RefreshToken;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.repository.RefreshTokenRepository;
import com.projetoWeb.Arenas.service.exception.RefreshTokenExpiredExpection;
import com.projetoWeb.Arenas.service.exception.RefreshTokenNotExistsException;
import com.projetoWeb.Arenas.service.user.UserService;

@Service
public class TokenService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private long accessTokenExpirationMinutes = 15;

    private long refreshTokenExpirationDays = 7;

    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities) {
        Instant now = Instant.now();

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("arenas_backend")
                .subject(email)
                .issuedAt(now)
                .expiresAt(now.plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES))
                .claim("scope", String.join(" ", roles))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Jwt validateToken(String token) {
        try {
            if (token == null) {
                throw new RuntimeException("Token não pode ser nulo");
            }
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            throw new RuntimeException("Token inválido");
        }
    }

    public void invalidateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    public String generateAndSaveRefreshToken(String email) {
        User user = userService.getUserByEmail(email);

        Optional<RefreshToken> optionalRefreshToken = this.refreshTokenRepository.findByUser(user);
        RefreshToken refreshToken;

        if (optionalRefreshToken.isPresent()) {
            refreshToken = optionalRefreshToken.get();
        } else {
            refreshToken = new RefreshToken();
            refreshToken.setUser(user);
        }

        refreshToken.setExpiryDate(Instant.now().plus(refreshTokenExpirationDays, ChronoUnit.DAYS));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from("refresh-token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpirationDays * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie generateResponseRefreshTokeCookieLogout() {
        return ResponseCookie.from("refresh-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    public String validateRefreshTokenReturnAccessToken(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);

        if (refreshTokenOpt.isEmpty()) {
            throw new RefreshTokenNotExistsException("O refresh token não existe");
        }

        RefreshToken refreshToken = refreshTokenOpt.get();
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenExpiredExpection("O refresh token está expirado");
        }

        return this.generateToken(refreshToken.getUser().getEmail(), refreshToken.getUser().getAuthorities());
    }

}
