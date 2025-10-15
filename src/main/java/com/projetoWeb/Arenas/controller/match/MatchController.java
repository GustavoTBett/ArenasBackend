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

    @GetMapping("/{id}")
    public ResponseEntity<Match> getById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.findAll());
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody MatchDto matchDto){
        return ResponseEntity.ok(matchService.create(matchDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long id, @RequestBody MatchDto matchDto){
        return ResponseEntity.ok(matchService.update(id, matchDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Match> cancelMatch(@PathVariable Long id, @RequestBody UserMatchDto matchDto){
        return ResponseEntity.ok(matchService.cancel(id, matchDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMatch(@PathVariable Long id, @RequestBody UserMatchDto matchDto){
        matchService.delete(id, matchDto);

        return ResponseEntity.ok().body("Deletado com sucesso");
    }
}
