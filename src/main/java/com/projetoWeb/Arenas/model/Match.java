package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.MatchStatusConverter;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "match")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime matchDate;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private Long maxPlayers;

    @JoinColumn(name = "creator_user_id", nullable = false)
    @ManyToOne
    private User createrUser;

    @Column(nullable = false)
    @Convert(converter = MatchStatusConverter.class)
    private MatchStatus matchStatus = MatchStatus.CONFIRMADA;
}
