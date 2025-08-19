package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.MatchLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "match_paramter")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long user_value;

    @Column(nullable = false)
    private MatchLevel matchLevel;

    @Column
    private Boolean privateMatch = false;

    @Column
    private Boolean notifyUser = false;

    @JoinColumn(name = "match_id", nullable = false)
    @OneToOne
    private Match match;
}
