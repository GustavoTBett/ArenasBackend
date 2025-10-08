package com.projetoWeb.Arenas.service;

import com.projetoWeb.Arenas.controller.dto.match.CreateMatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.repository.MatchRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    public List<Match> findAll(){
        return matchRepository.findAll();
    }

    public Match findById(long id){
        Optional<Match> match = matchRepository.findById(id);
        if (match.isPresent()){
            return match.get();
        }
        throw new EntityNotExistsException("Match Not Found");
    }

    public Match saveMatchAndamento(CreateMatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());

        Match match = Match.builder()
                .matchDate(matchDto.matchData())
                .title(matchDto.text())
                .maxPlayers(matchDto.maxPlayers())
                .description(matchDto.description())
                .createrUserId(user)
                .build();

        return matchRepository.save(match);
    }

    public Match update(Match match){
        return matchRepository.save(match);
    }

    public void deleteById(long id){
        matchRepository.deleteById(id);
    }
}
