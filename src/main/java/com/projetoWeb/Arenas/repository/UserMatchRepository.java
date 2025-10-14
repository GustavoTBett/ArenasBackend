package com.projetoWeb.Arenas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetoWeb.Arenas.model.UserMatch;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;

@Repository
public interface UserMatchRepository extends JpaRepository<UserMatch, Long> {

  List<UserMatch> findByMatchId(Long matchId);

  List<UserMatch> findByMatchIdAndUserMatchStatus(Long matchId, UserMatchStatus status);

  void deleteByMatchId(Long matchId);

}
