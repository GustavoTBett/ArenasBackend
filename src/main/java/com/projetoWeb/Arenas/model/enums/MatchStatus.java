package com.projetoWeb.Arenas.model.enums;

public enum MatchStatus {
    CONFIRMADA("A", "Confirmada"),
    FINALIZADA("F", "Finalizada"),
    CANCELADA("C", "Cancelada");

    private final String value;
    private final String descricao;

    MatchStatus(String value, String descricao) {
        this.value = value;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getValue() {
        return value;
    }

    public static MatchStatus fromString(String text) {
        for (MatchStatus status : MatchStatus.values()) {
            if (status.value.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant with value: " + text);
    }
}
