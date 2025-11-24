package com.projetoWeb.Arenas.controller.match.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResponderSolicitacaoRequest {
    @NotNull(message = "ID do UserMatch é obrigatório")
    private Long userMatchId;
    
    @NotNull(message = "Decisão é obrigatória (true para aceitar, false para recusar)")
    private Boolean aceitar;
    
    @NotNull(message = "ID do criador da partida é obrigatório")
    private Long creatorUserId;
}
