package com.projetoWeb.Arenas.controller.match.dto;

public record LocalMatchDto(String localName,
                             String localZipCode,
                             String localStreet,
                             String localNumber,
                             String localComplement,
                             String localCity,
                             String localState,
                             String localNeighborhood) {
}
