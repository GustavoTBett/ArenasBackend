package com.projetoWeb.Arenas.model.converter;

public enum MatchLevel {
    INICIANTE("Iniciante"),
    INTERMEDIARIO("Intermediário"),
    AVANCADO("Avançado"),
    MISTO("Misto");

    private String value;

    public String getValue() {
        return value;
    }

    MatchLevel(String value) {
        this.value = value;
    }
}
