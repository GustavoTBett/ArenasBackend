package com.projetoWeb.Arenas.repository;

import com.projetoWeb.Arenas.model.Match;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
  List<Match> findByMatchStatus(String status);
}
