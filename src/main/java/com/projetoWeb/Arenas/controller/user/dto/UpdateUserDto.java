package com.projetoWeb.Arenas.controller.user.dto;

import java.time.LocalDate;

import com.projetoWeb.Arenas.model.enums.RolePlayer;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        String profilePic,
        @Size(max = 5000, message = "Descrição do perfil não pode exceder 5000 caracteres") String profileDescripton,
        @Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve conter 10 ou 11 dígitos") String phone,
        @Size(max = 100, message = "Nome não pode exceder 100 caracteres") String firstName,
        @Size(max = 100, message = "Sobrenome não pode exceder 100 caracteres") String lastName,
        @Past(message = "Data de nascimento deve ser no passado") LocalDate birthDate,
        RolePlayer rolePlayer) {
}
