package com.projetoWeb.Arenas.model.enums;

public enum MatchStatus {
    AGENDADA("Agendada"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");

    private final String descricao;

    MatchStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
