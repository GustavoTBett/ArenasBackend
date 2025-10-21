package com.projetoWeb.Arenas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projetoWeb.Arenas.model.MatchParameter;

@Repository
public interface MatchParameterRepository extends JpaRepository<MatchParameter,Long> {
    Optional<MatchParameter> findByMatch_Id(long matchId);
}
