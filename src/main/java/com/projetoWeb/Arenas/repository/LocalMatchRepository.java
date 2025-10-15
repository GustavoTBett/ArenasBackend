package com.projetoWeb.Arenas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetoWeb.Arenas.model.LocalMatch;

@Repository
public interface LocalMatchRepository extends JpaRepository<LocalMatch, Long> {
    Optional<LocalMatch> findByMatch_Id(long matchId);
}
