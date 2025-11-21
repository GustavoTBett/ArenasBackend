package com.projetoWeb.Arenas.controller.userMatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserMatchDto (
    @NotNull(message = "Id do usuario e obrigatorio") Long userId,
    @NotNull(message = "Id da partida e obrigatorio") Long matchId,
    @NotBlank(message = "Posicao do jogador e obrigatorio") String rolePlayer,
    String userMatchStatus
) {
}
