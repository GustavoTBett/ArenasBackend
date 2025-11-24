package com.projetoWeb.Arenas.controller.match.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveMatchRequest {
    @NotNull(message = "ID da partida é obrigatório")
    private Long matchId;
    
    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;
}
