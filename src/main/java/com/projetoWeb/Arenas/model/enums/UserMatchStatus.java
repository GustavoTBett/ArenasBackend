package com.projetoWeb.Arenas.model.enums;

public enum UserMatchStatus {
    CONFIRMADO("A", "Confirmado"),
    SOLITADO("S", "Solicitado"),
    CONVITE_ENVIADO("C", "Convite Enviado"),
    RECUSADO("R", "Recusado");

    private String value;
    private String descricao;

    UserMatchStatus(String value, String descricao) {
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
