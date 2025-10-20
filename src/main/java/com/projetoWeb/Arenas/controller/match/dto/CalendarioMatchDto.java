package com.projetoWeb.Arenas.controller.match.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioMatchDto {

    private Long id;

    private String titulo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private ZonedDateTime dataHora;

    private MatchStatus status;

    private String local;

    private String participantes;
}

