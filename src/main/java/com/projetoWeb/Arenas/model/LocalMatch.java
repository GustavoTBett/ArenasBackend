package com.projetoWeb.Arenas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "local_match")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column(nullable = false, length = 8)
    private String zipCode;

    @JoinColumn(name = "match_id", nullable = false)
    @OneToOne
    private Match match;
}
