package com.projetoWeb.Arenas.controller;

import com.projetoWeb.Arenas.controller.dto.CreateUserDto;
import com.projetoWeb.Arenas.controller.dto.LoginRequestDto;
import com.projetoWeb.Arenas.controller.dto.ResponseUserDto;
import com.projetoWeb.Arenas.controller.dto.UpdateUserDto;
import com.projetoWeb.Arenas.model.RefreshToken;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsService userDetailsService;

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
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails instanceof User) {
            User user = (User) userDetails;
            ResponseUserDto responseUserDto = ResponseUserDto.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .role(user.getRole())
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

        ResponseCookie accessTokenCookie = tokenService.generateResponseCookieLogin(token);
        ResponseCookie refreshTokenCookie = tokenService.createRefreshTokenCookie(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Login bem-sucedido!");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refresh-token") String refreshToken) {
        ResponseCookie responseCookie = tokenService.validateRefreshToken(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refresh-token", required = false) String refreshToken) {
        if (refreshToken != null) {
            tokenService.invalidateRefreshToken(refreshToken);
        }

        ResponseCookie accessTokenCookie = tokenService.generateResponseCookieLogout();
        ResponseCookie refreshTokenCookie = tokenService.generateResponseRefreshTokeCookieLogout();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Logout bem-sucedido!");
    }
}