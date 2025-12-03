package com.projetoWeb.Arenas.controller.match.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MatchDto(
            @NotNull(message = "Data da partida é obrigatório") LocalDateTime matchData,
            @NotBlank(message = "Titulo da partida é obrigatório") String text,
            @NotNull(message = "Numero de jogadores é obrigatório") Long maxPlayers,
            String description,
            @NotNull(message = "Criador é obrigatório") Long creatorUserId,
            @Valid MatchParameterDto matchParameterDto,
            @Valid LocalMatchDto localMatchDto) {
}
