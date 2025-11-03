package com.projetoWeb.Arenas.controller.match.dto;


import jakarta.validation.constraints.NotNull;

public record UserMatchDto(@NotNull(message = "Criador e obrigatorio") Long creatorUserId) {
}
