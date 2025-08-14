package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.RolePlayerConverter;
import com.projetoWeb.Arenas.model.enums.RolePlayer;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "user_match")
@Builder
public class UserMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @Column(nullable = false)
    private Match match;

    @ManyToOne
    @Column(nullable = false)
    private User user;

    @Column(nullable = false)
    @Convert(converter = RolePlayerConverter.class)
    private RolePlayer rolePlayer;

    @Column(nullable = false)
    private UserMatchStatus userMatchStatus;
}
