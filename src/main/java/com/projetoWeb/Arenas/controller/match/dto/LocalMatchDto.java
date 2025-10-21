package com.projetoWeb.Arenas.controller.match.dto;

import jakarta.validation.constraints.NotBlank;

public record LocalMatchDto(
                @NotBlank(message = "Nome do local é obrigatório") String localName,
                @NotBlank(message = "Local da partida é obrigatório") String localZipCode,
                @NotBlank(message = "Rua do local é obrigatório") String localStreet,
                @NotBlank(message = "Numero do local é obrigatório") String localNumber,
                String localComplement,
                @NotBlank(message = "Cidade é obrigatória") String localCity,
                @NotBlank(message = "Estado é obrigatório") String localState,
                @NotBlank(message = "Bairro é obrigatório") String localNeighborhood) {
}
