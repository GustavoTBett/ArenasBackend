package com.projetoWeb.Arenas.model.converter;

public enum MatchLevel {
    INICIANTE("Iniciante"),
    INTERMEDIARIO("Intermediario"),
    AVANCADO("Avancado"),
    MISTO("Misto");

    private String value;

    public String getValue() {
        return value;
    }

    MatchLevel(String value) {
        this.value = value;
    }

    public static MatchLevel fromString(String text) {
        for (MatchLevel level : MatchLevel.values()) {
            if (level.value.equalsIgnoreCase(text)) {
                return level;
            }
        }
        throw new IllegalArgumentException("No enum constant with value: " + text);
    }
}
