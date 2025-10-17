package com.projetoWeb.Arenas.controller.match;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoWeb.Arenas.controller.match.dto.CalendarioMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.CreateMatchDto;
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

    @GetMapping("/getById/{id}")
    public ResponseEntity<Match> getById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.findAll());
    }

    @PostMapping("/andamento")
    public ResponseEntity<Match> createMatchAndamento(@RequestBody CreateMatchDto matchDto) {
        // return ResponseEntity.ok(matchService.saveMatchAndamento(matchDto));
        return null;
    }

    @PutMapping
    public ResponseEntity<Match> updateMatch(@RequestBody Match match) {
        return ResponseEntity.ok(matchService.update(match));
    }

    @DeleteMapping
    public ResponseEntity<Match> deleteMatch(@RequestBody Match match) {
        // return ResponseEntity.ok(matchService.)
        return null;
    }
}
