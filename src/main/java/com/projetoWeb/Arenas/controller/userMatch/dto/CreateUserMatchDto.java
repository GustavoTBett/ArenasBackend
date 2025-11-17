package com.projetoWeb.Arenas.controller.userMatch.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserMatchDto (
    @NotBlank(message = "Id do usuario e obrigatorio") Long userId,
    @NotBlank(message = "Id da partida e obrigatorio") Long matchId,
    @NotBlank(message = "Posicao do jogador e obrigatorio") String rolePlayer) {
}
