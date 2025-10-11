package com.projetoWeb.Arenas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetoWeb.Arenas.model.UserMatch;

@Repository
public interface UserMatchRepository extends JpaRepository<UserMatch, Long> {

  List<UserMatch> findByMatchId(Long matchId);

  void deleteByMatchId(Long matchId);

}
