package com.projetoWeb.Arenas.controller.dto;

import java.time.ZonedDateTime;

public record CreateMatchDto(ZonedDateTime matchData,
                             String text,
                             Long maxPlayers,
                             String description,
                             Long creatorUserId) {
}
