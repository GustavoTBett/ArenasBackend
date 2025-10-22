package com.projetoWeb.Arenas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
  List<Match> findByMatchStatus(MatchStatus status);

  static String sqlFindByUserAndMatchStatus = "" +
      " select m " +
      "   from Match m " +
      "   join UserMatch um on um.match = m " +
      "  where um.user.id = :userId " +
      "    and m.matchStatus = :status " +
      "  order by m.matchDate desc ";

  @Query(sqlFindByUserAndMatchStatus)
  List<Match> findByUserAndMatchStatus(@Param("userId") Long userId, @Param("status") MatchStatus status);

  static String sqlFindByUserAndUserMatchStatus = "" +
      " select m " +
      "   from Match m " +
      "   join UserMatch um on um.match = m " +
      "  where um.userMatchStatus = :userMatchStatus " +
      "    and m.createrUserId.id = :userId " +
      "    and m.matchStatus = :status " +
      "  order by m.matchDate desc ";

  @Query(sqlFindByUserAndUserMatchStatus)
  List<Match> findByUserAndUserMatchStatus(@Param("userId") Long userId, @Param("status") MatchStatus status,
      @Param("userMatchStatus") UserMatchStatus userMatchStatus);

  static String sqlFindByUserMatchAndMatchStatus = "" +
      " select m " +
      "   from Match m " +
      "   join UserMatch um on um.match = m " +
      "  where um.user.id = :userId " +
      "    and m.matchStatus = :status " +
      "  order by m.matchDate desc ";

  @Query(sqlFindByUserMatchAndMatchStatus)
  List<Match> findByUserMatchAndMatchStatus(@Param("userId") Long userId, @Param("status") MatchStatus status);

}
