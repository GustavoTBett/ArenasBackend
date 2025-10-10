package com.projetoWeb.Arenas.repository;

import com.projetoWeb.Arenas.model.LocalMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalMatchRepository extends JpaRepository<LocalMatch, Long> {
}
