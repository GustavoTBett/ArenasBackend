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
import com.projetoWeb.Arenas.controller.match.dto.MatchDto;
import com.projetoWeb.Arenas.controller.match.dto.ResponseSearchMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.SearchMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.UserMatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;
import com.projetoWeb.Arenas.service.match.MatchService;
import com.projetoWeb.Arenas.service.match.UserMatchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final UserMatchService userMatchService;

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
                    .build();

        }).toList();

        return ResponseEntity.ok().body(dtos);
    }

}
