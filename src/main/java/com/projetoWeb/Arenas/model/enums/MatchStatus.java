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
}
