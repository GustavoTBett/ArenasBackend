package com.projetoWeb.Arenas.model.enums;

public enum RolePlayer {
    GOLEIRO("goleiro", "Responsável por defender o gol."),
    ZAGUEIRO("zagueiro", "Atua na área central da defesa."),
    LATERAL_DIREITO("lateral direito", "Defende e ataca pelo lado direito do campo."),
    LATERAL_ESQUERDO("lateral esquerdo", "Defende e ataca pelo lado esquerdo do campo."),
    VOLANTE("volante", "Atua à frente da defesa, protegendo e iniciando jogadas."),
    MEIA("meia", "Jogador que atua no meio-campo, criando jogadas."),
    ATACANTE("atacante", "Responsável por finalizar as jogadas e marcar gols."),
    PONTA_DIREITA("ponta direita", "Atacante que joga aberto pelo lado direito."),
    PONTA_ESQUERDA("ponta esquerda", "Atacante que joga aberto pelo lado esquerdo."),
    CENTROAVANTE("centroavante", "Principal atacante, posicionado no centro da área adversária."),
    QUALQUER("qualquer", "Qualquer opção.");

    private final String nome;
    private final String descricao;

    RolePlayer(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

}
