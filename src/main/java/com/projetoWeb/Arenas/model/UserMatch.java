package com.projetoWeb.Arenas.model;

import com.projetoWeb.Arenas.model.converter.RolePlayerConverter;
import com.projetoWeb.Arenas.model.converter.UserMatchStatusConverter;
import com.projetoWeb.Arenas.model.enums.RolePlayer;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Column(nullable = false, length = 1)
    @Convert(converter = UserMatchStatusConverter.class)
    private UserMatchStatus userMatchStatus = UserMatchStatus.CONFIRMADO;
}
