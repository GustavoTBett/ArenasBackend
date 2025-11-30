package com.projetoWeb.Arenas.controller.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico para respostas de sucesso da API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private Boolean success;
    private String message;
}
