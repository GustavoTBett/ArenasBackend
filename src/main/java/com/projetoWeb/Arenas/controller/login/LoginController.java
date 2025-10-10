package com.projetoWeb.Arenas.controller.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoWeb.Arenas.controller.login.dto.LoginRequestDto;
import com.projetoWeb.Arenas.service.auth.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                loginRequest.email(), loginRequest.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken(auth.getName(), auth.getAuthorities());
        String refreshToken = tokenService.generateAndSaveRefreshToken(auth.getName());

        ResponseCookie refreshTokenCookie = tokenService.createRefreshTokenCookie(refreshToken);
        Map<String, String> accessToken = new HashMap<>();
        accessToken.put("accessToken", token);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(accessToken);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@CookieValue(name = "refresh-token") String refreshToken) {
        String token = tokenService.validateRefreshTokenReturnAccessToken(refreshToken);

        Map<String, String> accessToken = new HashMap<>();
        accessToken.put("accessToken", token);

        return ResponseEntity.ok()
                .body(accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = "refresh-token", required = false) String refreshToken) {
        if (refreshToken != null) {
            tokenService.invalidateRefreshToken(refreshToken);
        }

        ResponseCookie refreshTokenCookie = tokenService.generateResponseRefreshTokeCookieLogout();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Logout bem-sucedido!");
    }
}