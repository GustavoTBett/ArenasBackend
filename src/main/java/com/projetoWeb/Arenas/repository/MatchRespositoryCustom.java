package com.projetoWeb.Arenas.repository;

import java.util.List;

import com.projetoWeb.Arenas.model.Match;

public interface MatchRespositoryCustom {
  List<Match> searchMatches(
      String title,
      String status,
      String localName,
      String city,
      String zipCode,
      String matchLevel,
      String date,
      String time,
      Long userValue);
}
