package com.projetoWeb.Arenas.repository;

import com.projetoWeb.Arenas.model.LocalMatch;
import com.projetoWeb.Arenas.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalMatchRepository extends JpaRepository<LocalMatch, Long> {

    Optional<LocalMatch> findByMatch(Match match);
}
