package com.projetoWeb.Arenas.controller.dto;

import com.projetoWeb.Arenas.model.enums.RolePlayer;

import java.time.LocalDate;

public record UpdateUserDto(String profilePic, String profileDescripton, String phone, String firstName, String lastName,
                            LocalDate birthDate, RolePlayer rolePlayer) {
}
