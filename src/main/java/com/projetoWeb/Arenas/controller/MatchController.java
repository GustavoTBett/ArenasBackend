package com.projetoWeb.Arenas.controller;

import com.projetoWeb.Arenas.controller.dto.CreateMatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/getById/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        return ResponseEntity.ok(matchService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches(){
        return ResponseEntity.ok(matchService.findAll());
    }

    @PostMapping("/andamento")
    public ResponseEntity<Match> createMatchAndamento(@RequestBody CreateMatchDto matchDto){
//        return ResponseEntity.ok(matchService.saveMatchAndamento(matchDto));
        return null;
    }

    @PutMapping
    public ResponseEntity<Match> updateMatch(@RequestBody Match match){
        return ResponseEntity.ok(matchService.update(match));
    }

    @DeleteMapping
    public ResponseEntity<Match> deleteMatch(@RequestBody Match match){
//        return ResponseEntity.ok(matchService.)
        return null;
    }
}
