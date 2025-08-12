package com.projetoWeb.Arenas.model.enums;

public enum PermissaoEnums {
    BASICO("basic"),
    GERENCIADOR("manager"),
    ADMINISTRADOR("admin");

    private String value;

    public String getValue() {
        return value;
    }

    PermissaoEnums(String value) {
        this.value = value;
    }
}
