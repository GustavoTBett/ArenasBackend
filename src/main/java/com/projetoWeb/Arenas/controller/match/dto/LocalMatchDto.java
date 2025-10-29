package com.projetoWeb.Arenas.controller.match.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocalMatchDto(
                @NotNull(message = "Nome do local é obrigatório") @NotBlank(message = "Nome do local é obrigatório") String localName,
                @NotNull(message = "Local da partida é obrigatório") @NotBlank(message = "Local da partida é obrigatório") String localZipCode,
                @NotNull(message = "Rua do local é obrigatório") @NotBlank(message = "Rua do local é obrigatório") String localStreet,
                @NotNull(message = "Numero do local é obrigatório") @NotBlank(message = "Numero do local é obrigatório") String localNumber,
                String localComplement,
                @NotNull(message = "Cidade é obrigatório") @NotBlank(message = "Cidade é obrigatória") String localCity,
                @NotNull(message = "Estado é obrigatório") @NotBlank(message = "Estado é obrigatório") String localState,
                @NotNull(message = "Estado é obrigatório") @NotBlank(message = "Bairro é obrigatório") String localNeighborhood) {
}
