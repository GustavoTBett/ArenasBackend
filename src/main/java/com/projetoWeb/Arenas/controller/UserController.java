package com.projetoWeb.Arenas.controller;

import com.projetoWeb.Arenas.controller.dto.CreateUserDto;
import com.projetoWeb.Arenas.controller.dto.LoginRequestDto;
import com.projetoWeb.Arenas.controller.dto.UpdateUserDto;
import com.projetoWeb.Arenas.service.TokenService;
import com.projetoWeb.Arenas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController(value = "user")
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
    public ResponseEntity listUserById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@RequestParam("id") Long id, @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequest) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken(auth.getName());

        ResponseCookie cookie = tokenService.generateResponseCookieLogin(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login bem-sucedido!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = tokenService.generateResponseCookieLogout();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout bem-sucedido!");
    }
}