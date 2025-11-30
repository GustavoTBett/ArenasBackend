package com.projetoWeb.Arenas.controller.match.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO para requisição de atualização de posições dos jogadores
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePositionsRequest {
    @NotNull(message = "As posições são obrigatórias")
    private Map<String, Long> positions; // Mapa de posição -> userId (pode ser null para posição vazia)
}
