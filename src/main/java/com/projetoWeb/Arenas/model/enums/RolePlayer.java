package com.projetoWeb.Arenas.model.enums;

import com.projetoWeb.Arenas.model.converter.MatchLevel;

public enum RolePlayer {
    GOLEIRO("GOL", "Goleiro", "Responsável por defender o gol."),
    ZAGUEIRO("ZAG", "Zagueiro", "Atua na área central da defesa."),
    LATERAL_DIREITO("LD", "Lateral Direito", "Defende e ataca pelo lado direito do campo."),
    LATERAL_ESQUERDO("LE", "Lateral Esquerdo", "Defende e ataca pelo lado esquerdo do campo."),
    VOLANTE("VOL", "Volante", "Atua à frente da defesa, protegendo e iniciando jogadas."),
    MEIA("MEI", "Meia", "Jogador que atua no meio-campo, criando jogadas."),
    ATACANTE("ATA", "Atacante", "Responsável por finalizar as jogadas e marcar gols."),
    PONTA_DIREITA("PD", "Ponta Direita", "Atacante que joga aberto pelo lado direito."),
    PONTA_ESQUERDA("PE", "Ponta Esquerda", "Atacante que joga aberto pelo lado esquerdo.");

    private final String value;
    private final String nome;
    private final String descricao;

    RolePlayer(String value, String nome, String descricao) {
        this.value = value;
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getValue() {
        return value;
    }

    public static RolePlayer fromString(String text) {
        for (RolePlayer player: RolePlayer.values()) {
            if (player.value.equalsIgnoreCase(text)) {
                return player;
            }
        }
        throw new IllegalArgumentException("No enum constant with value: " + text);
    }
}
