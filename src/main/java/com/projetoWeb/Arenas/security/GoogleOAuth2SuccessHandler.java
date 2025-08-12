package com.projetoWeb.Arenas.security;

import com.projetoWeb.Arenas.model.Usuario;
import com.projetoWeb.Arenas.model.enums.PermissaoEnums;
import com.projetoWeb.Arenas.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Instant;

public class GoogleOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UsuarioRepository userRepository;
    private final JwtEncoder jwtEncoder;

    public GoogleOAuth2SuccessHandler(UsuarioRepository userRepository, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        var user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    Usuario novo = new Usuario();
                    novo.setEmail(email);
                    novo.setPassword("");
                    novo.setRole(PermissaoEnums.BASICO);
                    return userRepository.save(novo);
                });

        var now = Instant.now();
        var expiresIn = 18000000L;

        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", user.getRole())
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        HttpSession session = request.getSession(true); // cria sessão se não existir
        session.setAttribute("JWT_TOKEN", jwtValue);

        response.sendRedirect("http://localhost:3000/login-success);
    }
}