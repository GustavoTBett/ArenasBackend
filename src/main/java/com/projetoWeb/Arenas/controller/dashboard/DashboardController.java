package com.projetoWeb.Arenas.controller.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoWeb.Arenas.controller.dashboard.dto.ResponseDashboardDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import com.projetoWeb.Arenas.service.match.MatchService;
import com.projetoWeb.Arenas.service.match.UserMatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  @Autowired
  private MatchService matchService;

  @Autowired
  private UserMatchService userMatchService;

  @GetMapping("next-matchs")
  public ResponseEntity<List<ResponseDashboardDto>> getMatchsDashboard() {

    List<Match> matchs = matchService.findByStatus(MatchStatus.CONFIRMADA.getValue());

    List<ResponseDashboardDto> dtos = matchs.stream().map(match -> {

      return ResponseDashboardDto.builder()
          .id(match.getId())
          .createUserId(match.getCreaterUserId().getId())
          .createUserName(match.getCreaterUserId().getUsername())
          .title(match.getTitle())
          .time(match.getMatchDate().toLocalTime().toString())
          .date(match.getMatchDate().toLocalDate().toString())
          .maxPlayers(match.getMaxPlayers())
          .currentPlayers(userMatchService.countByMatchId(match.getId()))
          .status(match.getMatchStatus())
          .build();

    }).toList();

    return ResponseEntity.ok(dtos);
  }

}
