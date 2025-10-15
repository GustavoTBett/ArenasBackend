package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoWeb.Arenas.controller.match.dto.MatchParameterDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.MatchParameter;
import com.projetoWeb.Arenas.model.converter.MatchLevel;
import com.projetoWeb.Arenas.repository.MatchParameterRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;

@Service
public class MatchParameterService {

    @Autowired
    private MatchParameterRepository matchParameterRepository;

    @Autowired
    private MatchService matchService;

    public List<MatchParameter> findAll() {
        return matchParameterRepository.findAll();
    }

    public MatchParameter findById(long id) {
        Optional<MatchParameter> matchParameter = matchParameterRepository.findById(id);
        if (matchParameter.isPresent()) {
            return matchParameter.get();
        }

        throw new EntityNotExistsException("Match Parameter Not Found");
    }

    public MatchParameter findByMatchId(long id) {
        Optional<MatchParameter> matchParameter = matchParameterRepository.findByMatch_Id(id);
        if (matchParameter.isPresent()) {
            return matchParameter.get();
        }

        throw new EntityNotExistsException("Match Parameter Not Found");
    }

    public MatchParameter create(Long matchId, MatchParameterDto matchParameterDto){
        Match match = matchService.findById(matchId);

        MatchParameter matchParameter = MatchParameter.builder()
                .user_value(matchParameterDto.user_value())
                .matchLevel(MatchLevel.fromString(matchParameterDto.match_level()))
                .privateMatch(matchParameterDto.privateMatch())
                .notifyUser(matchParameterDto.notifyUser())
                .match(match)
                .build();

        return matchParameterRepository.save(matchParameter);
    }

    public MatchParameter updateByMatchId(Long matchId, MatchParameterDto matchParameterDto){
        Match match = matchService.findById(matchId);
        MatchParameter savedMatchParameter = findByMatchId(match.getId());

        MatchParameter matchParameter = MatchParameter.builder()
                .id(savedMatchParameter.getId())
                .user_value(matchParameterDto.user_value())
                .matchLevel(MatchLevel.fromString(matchParameterDto.match_level()))
                .privateMatch(matchParameterDto.privateMatch())
                .notifyUser(matchParameterDto.notifyUser())
                .match(match)
                .build();

        return matchParameterRepository.save(matchParameter);
    }

    public void deleteByMatchId(Long matchId){
        Match match = matchService.findById(matchId);
        MatchParameter savedMatchParameter = findByMatchId(match.getId());

        matchParameterRepository.deleteById(savedMatchParameter.getId());
    }
}
