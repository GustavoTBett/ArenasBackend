package com.projetoWeb.Arenas.controller.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO para detalhes da partida com jogadores e posições
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayersDetailDto {
    private Long id;
    private String title;
    private String date;
    private String time;
    private String status;
    private Long maxPlayers;
    private Long currentPlayers;
    private List<PlayerInfoDto> players;
    private Map<String, PlayerInfoDto> positions; // Mapa de posição -> jogador (pode ser null)
}
