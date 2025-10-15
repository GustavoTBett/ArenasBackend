package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoWeb.Arenas.controller.match.dto.DeleteMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.MatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.repository.MatchRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;
import com.projetoWeb.Arenas.service.user.UserService;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserService userService;

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

    public Match create(MatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());

        Match match = Match.builder()
                .matchDate(matchDto.matchData())
                .title(matchDto.text())
                .maxPlayers(matchDto.maxPlayers())
                .description(matchDto.description())
                .createrUser(user)
                .build();

        return matchRepository.save(match);
    }

    public Match update(Long id, MatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());
        findById(id);

        Match match = Match.builder()
                .id(id)
                .matchDate(matchDto.matchData())
                .title(matchDto.text())
                .maxPlayers(matchDto.maxPlayers())
                .description(matchDto.description())
                .createrUser(user)
                .build();

        return matchRepository.save(match);
    }

    public void delete(Long id, DeleteMatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());
        Match match = findById(id);

        if(user.getId() != match.getCreaterUser().getId()) {
            throw new EntityNotExistsException("Match Not Found");
        }

        matchRepository.deleteById(id);
    }
}
