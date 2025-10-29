package com.projetoWeb.Arenas.controller.match.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MatchParameterDto(
         @NotNull(message = "Valor por Pessoa é obrigatório") Long user_value,
         @NotNull(message = "Nivel da partida é obrigatório") @NotBlank(message = "Nivel da partida é obrigatório") String match_level,
         @NotNull(message = "Campo de Privacidade da partida é obrigatório") Boolean privateMatch,
         @NotNull(message = "Campo de Notificacao é obrigatório") Boolean notifyUser,
         @NotNull(message = "Campo de Recorrencia é obrigatório") Boolean reocuringMatch) {
}
