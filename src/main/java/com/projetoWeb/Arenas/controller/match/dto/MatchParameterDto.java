package com.projetoWeb.Arenas.controller.match.dto;

public record MatchParameterDto(
                             Long user_value,
                             String match_level,
                             Boolean privateMatch,
                             Boolean notifyUser,
                             Boolean reocuringMatch) {
}
