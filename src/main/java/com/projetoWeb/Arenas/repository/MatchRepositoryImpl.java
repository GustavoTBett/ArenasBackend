package com.projetoWeb.Arenas.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.QLocalMatch;
import com.projetoWeb.Arenas.model.QMatch;
import com.projetoWeb.Arenas.model.QMatchParameter;
import com.projetoWeb.Arenas.model.enums.MatchLevel;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class MatchRepositoryImpl implements MatchRespositoryCustom {

  private final JPAQueryFactory queryFactory;

  public MatchRepositoryImpl(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public List<Match> searchMatches(
      String title,
      String status,
      String localName,
      String city,
      String zipCode,
      String matchLevel,
      String date,
      String time,
      Long userValue) {

    QMatch match = QMatch.match;
    QLocalMatch localMatch = QLocalMatch.localMatch;
    QMatchParameter matchParameter = QMatchParameter.matchParameter;

    BooleanBuilder builder = new BooleanBuilder();

    if (title != null && !title.isEmpty()) {
      builder.and(match.title.containsIgnoreCase(title));
    }
    if (status != null && !status.isEmpty()) {
      builder.and(match.matchStatus.eq(MatchStatus.fromString(status)));
    }
    if (localName != null && !localName.isEmpty()) {
      builder.and(localMatch.description.containsIgnoreCase(localName));
    }
    if (city != null && !city.isEmpty()) {
      builder.and(localMatch.city.containsIgnoreCase(city));
    }
    if (zipCode != null && !zipCode.isEmpty()) {
      builder.and(localMatch.zipCode.eq(zipCode));
    }
    if (matchLevel != null && !matchLevel.isEmpty()) {
      builder.and(matchParameter.matchLevel.eq(MatchLevel.fromString(matchLevel)));
    }
    if (date != null && !date.isEmpty()) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      LocalDate parsedDate = LocalDate.parse(date, formatter);
      builder.and(match.matchDate.between(
          parsedDate.atStartOfDay(),
          parsedDate.plusDays(1).atStartOfDay()));
    }
    if (time != null && !time.isEmpty()) {
      int hour = Integer.parseInt(time.split(":")[0]);
      builder.and(match.matchDate.hour().eq(hour));
    }
    if (userValue != null) {
      builder.and(matchParameter.userValue.loe(userValue));
    }

    return queryFactory
        .selectFrom(match)
        .leftJoin(localMatch).on(localMatch.match.id.eq(match.id))
        .leftJoin(matchParameter).on(matchParameter.match.id.eq(match.id))
        .where(builder)
        .fetch();
  }

}
