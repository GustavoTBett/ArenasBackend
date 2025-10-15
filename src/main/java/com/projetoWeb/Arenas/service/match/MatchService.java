package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoWeb.Arenas.controller.match.dto.UserMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.MatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import com.projetoWeb.Arenas.repository.MatchRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;
import com.projetoWeb.Arenas.service.user.UserService;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MatchParameterService matchParameterService;

    @Autowired
    private LocalMatchService localMatchService;

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
                .description(matchDto.description())
                .maxPlayers(matchDto.maxPlayers())
                .createrUser(user)
                .build();
        Match savedMatch = matchRepository.save(match);

        matchParameterService.create(savedMatch.getId(), matchDto.matchParameterDto());
        localMatchService.create(savedMatch.getId(), matchDto.localMatchDto());

        return savedMatch;
    }

    public Match update(Long id, MatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());
        Match searchedMatch = findById(id);

        if(user.getId() != searchedMatch.getCreaterUser().getId()) {
            throw new EntityNotExistsException("Mis-matched user");
        }

        Match match = Match.builder()
                .id(id)
                .matchDate(matchDto.matchData())
                .title(matchDto.text())
                .maxPlayers(matchDto.maxPlayers())
                .description(matchDto.description())
                .createrUser(user)
                .build();
        Match savedMatch = matchRepository.save(match);

        matchParameterService.updateByMatchId(savedMatch.getId(), matchDto.matchParameterDto());
        localMatchService.updateByMatchId(savedMatch.getId(), matchDto.localMatchDto());

        return savedMatch;
    }

    public Match cancel(Long id, UserMatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());
        Match match = findById(id);

        if(user.getId() != match.getCreaterUser().getId()) {
            throw new EntityNotExistsException("Mis-matched user");
        }

        Match newMatch = Match.builder()
                .id(match.getId())
                .matchDate(match.getMatchDate())
                .title(match.getTitle())
                .maxPlayers(match.getMaxPlayers())
                .description(match.getDescription())
                .createrUser(match.getCreaterUser())
                .matchStatus(MatchStatus.CANCELADA)
                .build();

        return matchRepository.save(newMatch);
    }

    public void delete(Long id, UserMatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());
        Match match = findById(id);

        if(user.getId() != match.getCreaterUser().getId()) {
            throw new EntityNotExistsException("Mis-matched user");
        }

        matchRepository.deleteById(id);
        matchParameterService.deleteByMatchId(id);
        localMatchService.deleteByMatchId(id);
    }
}
