package com.projetoWeb.Arenas.controller.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para retornar partidas criadas pelo usuário
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchCreatedByUserDto {
    private Long id;
    private String title;
    private String date;
    private String time;
    private String status; // A=Ativa, C=Cancelada, F=Finalizada
    private Long maxPlayers;
    private Long currentPlayers;
    private String createUserName;
    private String description;
    
    // Campos de localização para construção do mapa
    private String localName;
    private String localZipCode;
    private String localStreet;
    private String localNumber;
    private String localComplement;
    private String localCity;
    private String localState;
    private String localNeighborhood;
}
