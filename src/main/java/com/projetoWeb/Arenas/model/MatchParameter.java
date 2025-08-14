package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.MatchLevel;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "match_paramter")
@Builder
public class MatchParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private Long user_value;

    @Column(nullable = false)
    private MatchLevel matchLevel;

    @Column
    private Boolean privateMatch = false;

    @Column
    private Boolean notifyUser = false;

    @Column(nullable = false)
    @OneToOne
    private Match match;
}
