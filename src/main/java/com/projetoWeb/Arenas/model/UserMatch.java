package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.RolePlayerConverter;
import com.projetoWeb.Arenas.model.enums.RolePlayer;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_match")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Convert(converter = RolePlayerConverter.class)
    private RolePlayer rolePlayer;

    @Column(nullable = false)
    private UserMatchStatus userMatchStatus;
}
