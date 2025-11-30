package com.projetoWeb.Arenas.controller.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para informações básicas do jogador
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInfoDto {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
}
