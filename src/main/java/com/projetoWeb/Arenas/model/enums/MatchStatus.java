package com.projetoWeb.Arenas.model.enums;

public enum MatchStatus {
    AGENDADA("Agendada"),
    EM_ANDAMENTO("Em Andamento"),
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
