package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.MatchStatusConverter;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "match")
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime matchDate;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private Long maxPlayers;

    @Column(nullable = false)
    @ManyToOne
    private User createrUserId;

    @Column(nullable = false)
    @Convert(converter = MatchStatusConverter.class)
    private MatchStatus matchStatus = MatchStatus.AGENDADA;


}
