package com.projetoWeb.Arenas.controller;

import com.projetoWeb.Arenas.controller.dto.match.CreateMatchDto;
import com.projetoWeb.Arenas.controller.dto.match.DeleteMatchDto;
import com.projetoWeb.Arenas.controller.dto.match.UpdateMatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<Match> getById(@PathVariable Long id){
        return ResponseEntity.ok(matchService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches(){
        return ResponseEntity.ok(matchService.findAll());
    }

    @PostMapping("/andamento")
    public ResponseEntity<Match> createMatchAndamento(@RequestBody CreateMatchDto matchDto){
        return ResponseEntity.ok(matchService.saveMatchAndamento(matchDto));
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
