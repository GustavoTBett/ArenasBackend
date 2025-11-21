package com.projetoWeb.Arenas.controller.userMatch;

import com.projetoWeb.Arenas.controller.userMatch.dto.CreateUserMatchDto;
import com.projetoWeb.Arenas.controller.userMatch.dto.PatchUserMatchDto;
import com.projetoWeb.Arenas.model.UserMatch;
import com.projetoWeb.Arenas.service.userMatch.UserMatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-match")
@RequiredArgsConstructor
public class UserMatchController {

    private final UserMatchService userMatchService;

    @GetMapping("/{userMatchId}")
    public ResponseEntity<UserMatch> getById(@PathVariable Long userMatchId) {
        return ResponseEntity.ok(userMatchService.findById(userMatchId));
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<UserMatch>> getByMatchId(@PathVariable Long matchId) {
        return ResponseEntity.ok(userMatchService.findByMatchId(matchId));
    }

    @GetMapping("/{userMatchId}/players-number")
    public ResponseEntity<Long> getNumberOfPlayers(@PathVariable Long userMatchId) {
        return ResponseEntity.ok(userMatchService.countByMatchId(userMatchId));
    }

    @PostMapping
    public ResponseEntity<UserMatch> createUserMatch(@Valid @RequestBody CreateUserMatchDto createUserMatchDto) {
        return ResponseEntity.ok(userMatchService.create(createUserMatchDto));
    }

    @PatchMapping("/{userMatchId}")
    public ResponseEntity<UserMatch> createUserMatch(@PathVariable Long userMatchId, @Valid @RequestBody PatchUserMatchDto patchUserMatchDto) {
        return ResponseEntity.ok(userMatchService.update(userMatchId, patchUserMatchDto));
    }
}
