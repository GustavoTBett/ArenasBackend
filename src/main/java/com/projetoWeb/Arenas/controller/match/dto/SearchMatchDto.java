package com.projetoWeb.Arenas.controller.match.dto;

public record SearchMatchDto(String title, String status, String localName, String city, String zipCode,
    String matchLevel, Long userValue) {
}
