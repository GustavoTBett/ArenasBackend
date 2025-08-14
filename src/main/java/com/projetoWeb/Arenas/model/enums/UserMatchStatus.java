package com.projetoWeb.Arenas.model.enums;

public enum UserMatchStatus {
    CONFIRMADO("Confirmado"),
    USUARIO_ENVIOU("Usuario pediu para entrar"),
    ORGANIZADOR_ENVIOU("Organizador pediu para usuário entrar");

    private String descricao;

    UserMatchStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
