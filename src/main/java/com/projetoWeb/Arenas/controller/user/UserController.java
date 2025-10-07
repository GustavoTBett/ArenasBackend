package com.projetoWeb.Arenas.controller.user;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projetoWeb.Arenas.controller.user.dto.CreateUserDto;
import com.projetoWeb.Arenas.controller.user.dto.ResponseUserDto;
import com.projetoWeb.Arenas.controller.user.dto.UpdateUserDto;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping()
  public ResponseEntity<Void> create(@Valid @RequestBody CreateUserDto dto) {
    Long userIdCreated = userService.createdUser(dto);
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("{id}")
        .buildAndExpand(userIdCreated)
        .toUri();
    return ResponseEntity.created(location).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> listUserById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Long> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserDto updateUserDto) {
    return ResponseEntity.ok(userService.updateUser(id, updateUserDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public ResponseEntity<ResponseUserDto> getCurrentUser(@AuthenticationPrincipal String email) {
    if (!"anonymousUser".equals(email)) {
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
}