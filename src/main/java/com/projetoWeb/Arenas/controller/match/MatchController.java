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

import com.projetoWeb.Arenas.controller.match.dto.CreateMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.DeleteMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.UpdateMatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.service.match.MatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<Match> getById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.findAll());
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody CreateMatchDto matchDto){
        return ResponseEntity.ok(matchService.create(matchDto));
    }

    @PutMapping
    public ResponseEntity<Match> updateMatch(@RequestBody UpdateMatchDto matchDto){
        return ResponseEntity.ok(matchService.update(matchDto));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteMatch(@RequestBody DeleteMatchDto matchDto){
        matchService.delete(matchDto);

        return ResponseEntity.ok().body("Deletado com sucesso");
    }
}
