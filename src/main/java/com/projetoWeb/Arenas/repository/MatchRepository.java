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

        static String sqlFindByUserAndMatchStatus = "" +
                        " select m " +
                        "   from Match m " +
                        "   left join UserMatch um on um.match = m " +
                        "  where (um.user.id = :userId or m.createrUserId.id = :userId) " +
                        "    and m.matchStatus = :status ";

        @Query(sqlFindByUserAndMatchStatus)
        List<Match> findByUserAndMatchStatus(@Param("userId") Long userId, @Param("status") MatchStatus status);

        static String sqlFindAllByUserId = "" +
                        " select m " +
                        "   from Match m " +
                        "   join UserMatch um on um.match = m " +
                        "  where um.user.id = :userId ";

        @Query(sqlFindAllByUserId)
        List<Match> findAllByUserId(@Param("userId") Long userId);
}
