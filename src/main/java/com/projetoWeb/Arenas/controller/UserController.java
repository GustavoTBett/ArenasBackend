package com.projetoWeb.Arenas.controller;

import com.projetoWeb.Arenas.controller.dto.CreateUserDto;
import com.projetoWeb.Arenas.controller.dto.LoginRequestDto;
import com.projetoWeb.Arenas.controller.dto.ResponseUserDto;
import com.projetoWeb.Arenas.controller.dto.UpdateUserDto;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.service.TokenService;
import com.projetoWeb.Arenas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping()
    public ResponseEntity create(@RequestBody CreateUserDto dto) {
        Long userIdCreated = userService.createdUser(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userIdCreated)
                .toUri();

        return ResponseEntity.created(location).build();
    }

//    @GetMapping("")
//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
//    public ResponseEntity<List<User>> listUsers() {
//        var users = userRepository.findAll();
//        return ResponseEntity.ok(users);
//    }

    @GetMapping("/{id}")
    public ResponseEntity listUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity getCurrentUser(@AuthenticationPrincipal String email) {
        if (email != null && !email.equals("anonymousUser")) {
            User user = userService.getUserByEmail(email);
            ResponseUserDto responseUserDto = ResponseUserDto.builder()
                    .email(email)
                    .role(user.getRole())
                    .firstName(user.getFirstName())
                    .build();
            return ResponseEntity.ok(responseUserDto);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequest) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
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
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refresh-token") String refreshToken) {
        String token = tokenService.validateRefreshTokenReturnAccessToken(refreshToken);
        Map<String, String> accessToken = new HashMap<>();
        accessToken.put("accessToken", token);

        return ResponseEntity.ok()
                .body(accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refresh-token", required = false) String refreshToken) {
        if (refreshToken != null) {
            tokenService.invalidateRefreshToken(refreshToken);
        }

        ResponseCookie refreshTokenCookie = tokenService.generateResponseRefreshTokeCookieLogout();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Logout bem-sucedido!");
    }
}