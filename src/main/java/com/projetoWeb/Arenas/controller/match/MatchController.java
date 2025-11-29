package com.projetoWeb.Arenas.controller.match;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoWeb.Arenas.controller.match.dto.CalendarioMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.LeaveMatchRequest;
import com.projetoWeb.Arenas.controller.match.dto.MatchDto;
import com.projetoWeb.Arenas.controller.match.dto.ResponderSolicitacaoRequest;
import com.projetoWeb.Arenas.controller.match.dto.ResponseSearchMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.SearchMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.SolicitacaoMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.UserMatchDto;
import com.projetoWeb.Arenas.model.LocalMatch;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.MatchParameter;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.model.UserMatch;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;
import com.projetoWeb.Arenas.service.match.LocalMatchService;
import com.projetoWeb.Arenas.service.match.MatchParameterService;
import com.projetoWeb.Arenas.service.match.MatchService;
import com.projetoWeb.Arenas.service.userMatch.UserMatchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final UserMatchService userMatchService;
    private final LocalMatchService localMatchService;
    private final MatchParameterService matchParameterService;

    @GetMapping("/calendario/{userId}")
    public ResponseEntity<List<CalendarioMatchDto>> getCalendarioMatches(@PathVariable Long userId) {
        return ResponseEntity.ok(matchService.findAllForCalendario(userId));
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<Match> getById(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.findById(matchId));
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.findAll());
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@Valid @RequestBody MatchDto matchDto) {
        return ResponseEntity.ok(matchService.create(matchDto));
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long matchId, @Valid @RequestBody MatchDto matchDto) {
        return ResponseEntity.ok(matchService.update(matchId, matchDto));
    }

    @PatchMapping("/{matchId}")
    public ResponseEntity<Match> cancelMatch(@PathVariable Long matchId, @Valid @RequestBody UserMatchDto matchDto) {
        return ResponseEntity.ok(matchService.cancel(matchId, matchDto));
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<String> deleteMatch(@PathVariable Long matchId, @Valid @RequestBody UserMatchDto matchDto) {
        matchService.delete(matchId, matchDto);

        return ResponseEntity.ok().body("Deletado com sucesso");
    }

    @PostMapping("/search")
    public ResponseEntity<List<ResponseSearchMatchDto>> searchMatch(@RequestBody SearchMatchDto searchMatchDto) {
        List<Match> matches = matchService.searchMatches(searchMatchDto);

        if (matches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ResponseSearchMatchDto> dtos = matches.stream().map(match -> {

            Long currentPlayers = userMatchService.countByMatchIdAndUserMatchStatus(match.getId(),
                    UserMatchStatus.CONFIRMADO);

            String createdUserName = match.getCreaterUserId().getFirstName() + " "
                    + match.getCreaterUserId().getLastName();

            String localName = null;
            String localZipCode = null;
            String localStreet = null;
            String localNumber = null;
            String localComplement = null;
            String localCity = null;
            String localState = null;
            String localNeighborhood = null;

            Boolean privateMatch = false;

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

                MatchParameter matchParameter = matchParameterService.findByMatchId(match.getId());
                if (matchParameter != null) {
                    privateMatch = matchParameter.getPrivateMatch();
                }
            } catch (Exception e) {
            }

            return ResponseSearchMatchDto.builder()
                    .id(match.getId())
                    .createUserId(match.getCreaterUserId().getId())
                    .createUserName(createdUserName)
                    .title(match.getTitle())
                    .time(match.getMatchDate().toLocalTime().toString())
                    .date(match.getMatchDate().toLocalDate().toString())
                    .maxPlayers(match.getMaxPlayers())
                    .currentPlayers(currentPlayers)
                    .status(match.getMatchStatus().getValue())
                    .localName(localName)
                    .localZipCode(localZipCode)
                    .localStreet(localStreet)
                    .localNumber(localNumber)
                    .localComplement(localComplement)
                    .localCity(localCity)
                    .localState(localState)
                    .localNeighborhood(localNeighborhood)
                    .privateMatch(privateMatch)
                    .players(userMatchService.findByMatchId(match.getId()))
                    .build();

        }).toList();

        return ResponseEntity.ok().body(dtos);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<String> leaveMatch(@Valid @RequestBody LeaveMatchRequest request) {
        userMatchService.leaveMatch(request.getMatchId(), request.getUserId());
        return ResponseEntity.ok().body("Você saiu da partida com sucesso");
    }

    @GetMapping("/solicitacoes/{creatorUserId}")
    public ResponseEntity<List<SolicitacaoMatchDto>> getSolicitacoes(
            @PathVariable Long creatorUserId) {

        List<UserMatch> solicitacoes = userMatchService.findSolicitacoesByCreator(creatorUserId);

        if (solicitacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<SolicitacaoMatchDto> dtos = solicitacoes.stream().map(userMatch -> {
            User user = userMatch.getUser();

            String profilePicBase64 = null;
            if (user.getProfilePic() != null && user.getProfilePic().length > 0) {
                profilePicBase64 = java.util.Base64.getEncoder().encodeToString(user.getProfilePic());
            }

            return SolicitacaoMatchDto.builder()
                    .userMatchId(userMatch.getId())
                    .matchId(userMatch.getMatch().getId())
                    .matchTitle(userMatch.getMatch().getTitle())
                    .matchDate(userMatch.getMatch().getMatchDate())
                    .userId(user.getId())
                    .userFirstName(user.getFirstName())
                    .userLastName(user.getLastName())
                    .userEmail(user.getEmail())
                    .userProfilePic(profilePicBase64)
                    .rolePlayer(userMatch.getRolePlayer())
                    .build();
        }).toList();

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/solicitacoes/responder")
    public ResponseEntity<String> responderSolicitacao(
            @Valid @RequestBody ResponderSolicitacaoRequest request) {

        userMatchService.responderSolicitacao(
                request.getUserMatchId(),
                request.getAceitar(),
                request.getCreatorUserId());

        String mensagem = request.getAceitar()
                ? "Solicitação aceita com sucesso"
                : "Solicitação recusada";

        return ResponseEntity.ok(mensagem);
    }

}
