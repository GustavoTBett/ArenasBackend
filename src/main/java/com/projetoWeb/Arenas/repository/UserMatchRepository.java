package com.projetoWeb.Arenas.repository;

import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.UserMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMatchRepository extends JpaRepository<UserMatch, Long> {

    List<UserMatch> findByMatch(Match match);

    long countByMatch(Match match);
}

