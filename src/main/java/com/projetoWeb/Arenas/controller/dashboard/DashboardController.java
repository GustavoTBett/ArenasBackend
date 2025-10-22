package com.projetoWeb.Arenas.controller.dashboard;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoWeb.Arenas.controller.dashboard.dto.ResponseDashboardDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.UserMatch;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;
import com.projetoWeb.Arenas.service.match.LocalMatchService;
import com.projetoWeb.Arenas.service.match.MatchService;
import com.projetoWeb.Arenas.service.match.UserMatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final MatchService matchService;
  private final UserMatchService userMatchService;
  private final LocalMatchService localMatchService;

  @GetMapping("next-matches/{userId}")
  public ResponseEntity<List<ResponseDashboardDto>> getNextMacths(@PathVariable Long userId) {

    List<Match> matchs = matchService.findByUserAndMatchStatus(userId, MatchStatus.CONFIRMADA);

    List<ResponseDashboardDto> dtos = matchs.stream().map(match -> {

      Long currentPlayers = userMatchService.countByMatchIdAndUserMatchStatus(match.getId(),
          UserMatchStatus.CONFIRMADO);

      String createdUserName = match.getCreaterUserId().getFirstName() + " " + match.getCreaterUserId().getLastName();

      return ResponseDashboardDto.builder()
          .id(match.getId())
          .createUserId(match.getCreaterUserId().getId())
          .createUserName(createdUserName)
          .title(match.getTitle())
          .time(match.getMatchDate().toLocalTime().toString())
          .date(match.getMatchDate().toLocalDate().toString())
          .maxPlayers(match.getMaxPlayers())
          .currentPlayers(currentPlayers)
          .localMatch(localMatchService.findByMatchId(match.getId()))
          .status(match.getMatchStatus().getValue())
          .build();

    }).toList();

    return ResponseEntity.ok().body(dtos);
  }

  @GetMapping("matches-requests/{userId}")
  public ResponseEntity<List<ResponseDashboardDto>> getMatchesRequests(@PathVariable Long userId) {

    List<Match> matchs = matchService.findByUserAndUserMatchStatus(userId, MatchStatus.CONFIRMADA,
        UserMatchStatus.PENDENTE);

    List<ResponseDashboardDto> dtos = matchs.stream().map(match -> {

      Long currentPlayers = userMatchService.countByMatchIdAndUserMatchStatus(match.getId(),
          UserMatchStatus.CONFIRMADO);

      UserMatch userMatch = userMatchService.findByUserIdAndMatchId(userId, match.getId());
      String requestedUserName = userMatch.getUser().getFirstName() + " " + userMatch.getUser().getLastName();

      return ResponseDashboardDto.builder()
          .id(match.getId())
          .createUserId(match.getCreaterUserId().getId())
          .requestedUserName(requestedUserName)
          .title(match.getTitle())
          .time(match.getMatchDate().toLocalTime().toString())
          .date(match.getMatchDate().toLocalDate().toString())
          .maxPlayers(match.getMaxPlayers())
          .currentPlayers(currentPlayers)
          .localMatch(localMatchService.findByMatchId(match.getId()))
          .status(UserMatchStatus.PENDENTE.getValue())
          .build();

    }).toList();

    return ResponseEntity.ok().body(dtos);
  }

  @GetMapping("matches-history/{userId}")
  public ResponseEntity<List<ResponseDashboardDto>> getMethodName(@PathVariable Long userId) {

    List<Match> matchs = matchService.findByUserMatchAndMatchStatus(userId, MatchStatus.FINALIZADA);

    List<ResponseDashboardDto> dtos = matchs.stream().map(match -> {

      return ResponseDashboardDto.builder()
          .id(match.getId())
          .createUserId(match.getCreaterUserId().getId())
          .title(match.getTitle())
          .time(match.getMatchDate().toLocalTime().toString())
          .date(match.getMatchDate().toLocalDate().toString())
          .maxPlayers(match.getMaxPlayers())
          .localMatch(localMatchService.findByMatchId(match.getId()))
          .status(MatchStatus.FINALIZADA.getValue())
          .build();

    }).toList();

    return ResponseEntity.ok().body(dtos);
  }

}
