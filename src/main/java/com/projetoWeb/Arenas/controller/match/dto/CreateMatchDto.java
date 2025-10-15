package com.projetoWeb.Arenas.controller.match.dto;

import java.time.ZonedDateTime;

public record CreateMatchDto(ZonedDateTime matchData,
                             String text,
                             Long maxPlayers,
                             String description,
                             Long creatorUserId,
                             Long user_value,
                             String match_level,
                             Boolean privateMatch,
                             Boolean notifyUser,
                             Boolean reocuringMatch,
                             String localName,
                             String localZipCode,
                             String localCode,
                             String localComplement,
                             String localCity,
                             String localState,
                             String localNeighborhood) {
}
