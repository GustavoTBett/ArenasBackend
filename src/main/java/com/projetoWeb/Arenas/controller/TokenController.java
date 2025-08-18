//package com.projetoWeb.Arenas.controller;
//
//import com.projetoWeb.Arenas.controller.dto.LoginResponse;
//import com.projetoWeb.Arenas.repository.UserRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.Instant;
//import java.util.Map;
//
//@RestController(value = "token")
//public class TokenController {
//
//    private final JwtEncoder jwtEncoder;
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
//        this.jwtEncoder = jwtEncoder;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @GetMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestParam("username") String username,
//                                               @RequestParam("password") String password,
//                                               HttpServletRequest request) {
//        var user = userRepository.findByEmail(username);
//
//        if (user.isEmpty() || !user.get().isLoginCorrect(password, passwordEncoder)) {
//            throw new BadCredentialsException("user or password is invalid!");
//        }
//
//        var now = Instant.now();
//        var expiresIn = 18000000L;
//
//        var scopes = user.get().getRole();
//
//        var claims = JwtClaimsSet.builder()
//                .issuer("mybackend")
//                .subject(user.get().getId().toString())
//                .issuedAt(now)
//                .expiresAt(now.plusSeconds(expiresIn))
//                .claim("scope", scopes)
//                .build();
//
//        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//
//        HttpSession session = request.getSession(true);
//        session.setAttribute("JWT_TOKEN", jwtValue);
//
//        return ResponseEntity.ok(LoginResponse.builder()
//                .accessToken(jwtValue)
//                .expiresIn(expiresIn)
//                .build());
//    }
//
//    @GetMapping("/refresh")
//    public ResponseEntity refreshTken(HttpSession session) {
//        String jwtToken = (String) session.getAttribute("JWT_TOKEN");
//        if (jwtToken == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        return ResponseEntity.ok(Map.of("token", jwtToken));
//    }
//
//}