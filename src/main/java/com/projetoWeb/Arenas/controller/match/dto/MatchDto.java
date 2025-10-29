package com.projetoWeb.Arenas.controller.match.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public record MatchDto(
            @NotNull(message = "Data da partida é obrigatório") ZonedDateTime matchData,
            @NotNull(message = "Titulo da partida é obrigatório") @NotBlank(message = "Titulo da partida é obrigatório") String text,
            @NotNull(message = "Numero de jogadores é obrigatório") Long maxPlayers,
            String description,
            @NotNull(message = "Criador é obrigatório") Long creatorUserId,
            MatchParameterDto matchParameterDto,
            LocalMatchDto localMatchDto) {
}
