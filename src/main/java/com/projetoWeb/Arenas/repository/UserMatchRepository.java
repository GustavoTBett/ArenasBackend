package com.projetoWeb.Arenas.repository;

import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.UserMatch;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetoWeb.Arenas.model.enums.UserMatchStatus;

@Repository
public interface UserMatchRepository extends JpaRepository<UserMatch, Long> {

  List<UserMatch> findByMatch(Match match);
  List<UserMatch> findByMatchId(Long matchId);
  List<UserMatch> findByMatchIdAndUserId(Long matchId, Long userId);

  List<UserMatch> findByMatchIdAndUserMatchStatus(Long matchId, UserMatchStatus status);

  void deleteByMatchId(Long matchId);

  long countByMatch(Match match);
}

