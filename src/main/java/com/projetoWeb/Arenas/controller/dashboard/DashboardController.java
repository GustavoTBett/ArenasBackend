package com.projetoWeb.Arenas.controller.dashboard;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoWeb.Arenas.controller.dashboard.dto.ResponseDashboardDto;
import com.projetoWeb.Arenas.controller.userMatch.dto.SearchUserMatchDto;
import com.projetoWeb.Arenas.model.LocalMatch;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.UserMatch;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;
import com.projetoWeb.Arenas.service.match.LocalMatchService;
import com.projetoWeb.Arenas.service.match.MatchService;
import com.projetoWeb.Arenas.service.userMatch.UserMatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final MatchService matchService;
  private final UserMatchService userMatchService;
  private final LocalMatchService localMatchService;

  /**
   * Busca as próximas partidas do usuário para o Dashboard.
   * 
   * IMPORTANTE: Retorna APENAS partidas onde o usuário está vinculado como jogador
   * através da tabela UserMatch com status CONFIRMADO.
   * Não retorna partidas apenas porque o usuário é o criador.
   * 
   * @param userId ID do usuário
   * @return Lista de partidas confirmadas onde o usuário participa como jogador
   */
  @GetMapping("next-matches/{userId}")
  public ResponseEntity<List<ResponseDashboardDto>> getNextMacths(@PathVariable Long userId) {

    List<Match> matchs = matchService.findByUserAndMatchStatus(userId, MatchStatus.CONFIRMADA);

    if (matchs.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    List<ResponseDashboardDto> dtos = matchs.stream().map(match -> {

      Long currentPlayers = userMatchService.countByMatchIdAndUserMatchStatus(match.getId(),
          UserMatchStatus.CONFIRMADO);

      String createdUserName = match.getCreaterUserId().getFirstName() + " " + match.getCreaterUserId().getLastName();

      String localName = null;
      String localZipCode = null;
      String localStreet = null;
      String localNumber = null;
      String localComplement = null;
      String localCity = null;
      String localState = null;
      String localNeighborhood = null;

      try {
        LocalMatch localMatch = localMatchService.findByMatchId(match.getId());
        if (localMatch != null) {
          localName = localMatch.getDescription();
          localZipCode = localMatch.getZipCode();
          localStreet = localMatch.getStreet();
          localNumber = localMatch.getNumber();
          localComplement = localMatch.getComplement();
          localCity = localMatch.getCity();
          localState = localMatch.getState();
          localNeighborhood = localMatch.getNeighborhood();
        }
      } catch (Exception e) {
      }

      SearchUserMatchDto searchUserMatchDto = new SearchUserMatchDto(userId, match.getId());
      List<UserMatch> userMatch = userMatchService.findByUserAndMatch(searchUserMatchDto);

      return ResponseDashboardDto.builder()
          .id(match.getId())
          .createUserId(match.getCreaterUserId().getId())
          .createUserName(createdUserName)
          .title(match.getTitle())
          .time(match.getMatchDate().toLocalTime().toString())
          .date(match.getMatchDate().toLocalDate().toString())
          .maxPlayers(match.getMaxPlayers())
          .currentPlayers(currentPlayers)
          .status(
              !userMatch.isEmpty() && userMatch.get(0) != null ? userMatch.get(0).getUserMatchStatus().getValue() : "A")
          .localName(localName)
          .localZipCode(localZipCode)
          .localStreet(localStreet)
          .localNumber(localNumber)
          .localComplement(localComplement)
          .localCity(localCity)
          .localState(localState)
          .localNeighborhood(localNeighborhood)
          .build();

    }).toList();

    return ResponseEntity.ok().body(dtos);
  }

  @GetMapping("matches-history/{userId}")
  public ResponseEntity<List<ResponseDashboardDto>> getMethodName(@PathVariable Long userId) {

    List<Match> matchs = matchService.findByUserAndMatchStatus(userId, MatchStatus.FINALIZADA);

    List<ResponseDashboardDto> dtos = matchs.stream().map(match -> {

      Long currentPlayers = userMatchService.countByMatchIdAndUserMatchStatus(match.getId(),
          UserMatchStatus.CONFIRMADO);

      String createdUserName = match.getCreaterUserId().getFirstName() + " " + match.getCreaterUserId().getLastName();

      String localName = null;
      String localZipCode = null;
      String localStreet = null;
      String localNumber = null;
      String localComplement = null;
      String localCity = null;
      String localState = null;
      String localNeighborhood = null;

      try {
        LocalMatch localMatch = localMatchService.findByMatchId(match.getId());
        if (localMatch != null) {
          localName = localMatch.getDescription();
          localZipCode = localMatch.getZipCode();
          localStreet = localMatch.getStreet();
          localNumber = localMatch.getNumber();
          localComplement = localMatch.getComplement();
          localCity = localMatch.getCity();
          localState = localMatch.getState();
          localNeighborhood = localMatch.getNeighborhood();
        }
      } catch (Exception e) {
      }

      SearchUserMatchDto searchUserMatchDto = new SearchUserMatchDto(userId, match.getId());
      List<UserMatch> userMatch = userMatchService.findByUserAndMatch(searchUserMatchDto);

      return ResponseDashboardDto.builder()
          .id(match.getId())
          .createUserId(match.getCreaterUserId().getId())
          .createUserName(createdUserName)
          .title(match.getTitle())
          .time(match.getMatchDate().toLocalTime().toString())
          .date(match.getMatchDate().toLocalDate().toString())
          .maxPlayers(match.getMaxPlayers())
          .currentPlayers(currentPlayers)
          .status(
              !userMatch.isEmpty() && userMatch.get(0) != null ? userMatch.get(0).getUserMatchStatus().getValue() : "A")
          .localName(localName)
          .localZipCode(localZipCode)
          .localStreet(localStreet)
          .localNumber(localNumber)
          .localComplement(localComplement)
          .localCity(localCity)
          .localState(localState)
          .localNeighborhood(localNeighborhood)
          .build();

    }).toList();

    return ResponseEntity.ok().body(dtos);
  }

}
