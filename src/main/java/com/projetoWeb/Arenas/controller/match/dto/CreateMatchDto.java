package com.projetoWeb.Arenas.controller.match.dto;

import java.time.ZonedDateTime;

public record CreateMatchDto(ZonedDateTime matchData,
    String text,
    Long maxPlayers,
    String description,
    Long creatorUserId) {
}
