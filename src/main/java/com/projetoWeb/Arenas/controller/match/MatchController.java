package com.projetoWeb.Arenas.controller.match;

import java.util.List;

import com.projetoWeb.Arenas.controller.match.dto.CalendarioMatchDto;
import jakarta.validation.Valid;
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

import com.projetoWeb.Arenas.controller.match.dto.UserMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.MatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.service.match.MatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/calendario")
    public ResponseEntity<List<CalendarioMatchDto>> getCalendarioMatches() {
        return ResponseEntity.ok(matchService.findAllForCalendario());
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
    public ResponseEntity<Match> createMatch(@RequestBody MatchDto matchDto){
        return ResponseEntity.ok(matchService.create(matchDto));
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long matchId, @RequestBody MatchDto matchDto){
        return ResponseEntity.ok(matchService.update(matchId, matchDto));
    }

    @PatchMapping("/{matchId}")
    public ResponseEntity<Match> cancelMatch(@PathVariable Long matchId, @RequestBody UserMatchDto matchDto){
        return ResponseEntity.ok(matchService.cancel(matchId, matchDto));
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<String> deleteMatch(@PathVariable Long matchId, @RequestBody UserMatchDto matchDto){
        matchService.delete(matchId, matchDto);

        return ResponseEntity.ok().body("Deletado com sucesso");
    }
}
