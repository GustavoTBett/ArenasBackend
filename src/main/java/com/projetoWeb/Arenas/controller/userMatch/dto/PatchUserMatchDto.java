package com.projetoWeb.Arenas.controller.userMatch.dto;

import jakarta.validation.constraints.NotBlank;

public record PatchUserMatchDto (
    @NotBlank(message = "Status da Partida e obrigatorio") String userMatchStatus
) {
}
