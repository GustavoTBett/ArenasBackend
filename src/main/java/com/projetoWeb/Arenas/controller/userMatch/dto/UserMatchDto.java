package com.projetoWeb.Arenas.controller.userMatch.dto;

import jakarta.validation.constraints.NotBlank;

public record UserMatchDto(
    @NotBlank(message = "id do usuario e obrigatorio") Long userId,
    @NotBlank(message = "id da partida e obrigatorio") Long matchId,
    @NotBlank(message = "posicao do jogador e obrigatorio") String rolePlayer) {
}
