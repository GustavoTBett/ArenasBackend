package com.projetoWeb.Arenas.controller.dto;

import com.projetoWeb.Arenas.model.enums.MatchStatus;

import java.time.ZonedDateTime;

public record CreateMatchDto(ZonedDateTime matchData,
                             String text,
                             Long maxPlayers,
                             String description,
                             Long creatorUserId) {
}
