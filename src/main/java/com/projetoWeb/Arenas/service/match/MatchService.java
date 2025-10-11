package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.repository.MatchRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public Match findById(long id) {
        Optional<Match> match = matchRepository.findById(id);
        if (match.isPresent()) {
            return match.get();
        }
        throw new EntityNotExistsException("Match Not Found");
    }

    // public Match saveMatchAndamento(CreateMatchDto matchDto){
    // User user = userDetailsService.
    // Match match = Match.builder()
    // .matchDate(matchDto.matchData())
    // .title(matchDto.text())
    // .maxPlayers(matchDto.maxPlayers())
    // .description(matchDto.description())
    // .createrUserId(matchDto.creatorUserId())
    //
    // build();
    // return matchRepository.save(match);
    // }

    public Match update(Match match) {
        return matchRepository.save(match);
    }

    public void deleteById(long id) {
        matchRepository.deleteById(id);
    }

    public List<Match> findByStatus(String status) {
        return matchRepository.findByMatchStatus(status);
    }
}
