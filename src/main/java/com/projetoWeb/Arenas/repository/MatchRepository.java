package com.projetoWeb.Arenas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.enums.MatchStatus;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>, MatchRespositoryCustom {
        List<Match> findByMatchStatus(MatchStatus status);

        /**
         * Busca partidas onde o usuário está vinculado como jogador (através de UserMatch)
         * e a partida possui o status especificado.
         * Retorna apenas partidas onde o usuário tem status CONFIRMADO em UserMatch.
         */
        static String sqlFindByUserAndMatchStatus = "" +
                        " select distinct m " +
                        "   from Match m " +
                        "   join UserMatch um on um.match = m " +
                        "  where um.user.id = :userId " +
                        "    and m.matchStatus = :status " +
                        "    and (um.userMatchStatus = 'C' or um.userMatchStatus = 'S') ";

        @Query(sqlFindByUserAndMatchStatus)
        List<Match> findByUserAndMatchStatus(@Param("userId") Long userId, @Param("status") MatchStatus status);

        static String sqlFindAllByUserId = "" +
                        " select m " +
                        "   from Match m " +
                        "   join UserMatch um on um.match = m " +
                        "  where um.user.id = :userId and m.matchStatus = 'A' ";

        @Query(sqlFindAllByUserId)
        List<Match> findAllByUserId(@Param("userId") Long userId);

        /**
         * Busca todas as partidas criadas por um usuário específico
         */
        List<Match> findByCreaterUserId_IdAndMatchStatus(Long creatorUserId, MatchStatus matchStatus);
}
