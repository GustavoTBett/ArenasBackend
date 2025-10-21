package com.projetoWeb.Arenas.controller.match.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.ZonedDateTime;

public record MatchDto(
            @NotBlank(message = "Data da partida é obrigatório") ZonedDateTime matchData,
            @NotBlank(message = "Titulo da partida é obrigatório") String text,
            Long maxPlayers,
            String description,
            Long creatorUserId,
            MatchParameterDto matchParameterDto,
            LocalMatchDto localMatchDto) {
}
