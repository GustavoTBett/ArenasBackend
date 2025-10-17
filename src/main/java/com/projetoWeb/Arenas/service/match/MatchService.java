package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetoWeb.Arenas.controller.match.dto.CalendarioMatchDto;
import com.projetoWeb.Arenas.model.LocalMatch;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.repository.LocalMatchRepository;
import com.projetoWeb.Arenas.repository.MatchRepository;
import com.projetoWeb.Arenas.repository.UserMatchRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private LocalMatchRepository localMatchRepository;

    @Autowired
    private UserMatchRepository userMatchRepository;

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

    @Transactional(readOnly = true)
    public List<CalendarioMatchDto> findAllForCalendario() {
        List<Match> matches = matchRepository.findAll();

        return matches.stream()
            .map(this::convertToCalendarioDto)
            .collect(Collectors.toList());
    }

    private CalendarioMatchDto convertToCalendarioDto(Match match) {
        // Busca informações de local
        String localInfo = getLocalInfo(match);

        // Calcula participantes (atual/máximo)
        long participantesAtuais = userMatchRepository.countByMatch(match);
        String participantesInfo = participantesAtuais + "/" + match.getMaxPlayers();

        return CalendarioMatchDto.builder()
            .id(match.getId())
            .titulo(match.getTitle())
            .dataHora(match.getMatchDate())
            .status(match.getMatchStatus())
            .local(localInfo)
            .participantes(participantesInfo)
            .build();
    }

    private String getLocalInfo(Match match) {
        try {
            Optional<LocalMatch> localMatch = localMatchRepository.findByMatch(match);

            if (localMatch.isPresent()) {
                LocalMatch local = localMatch.get();
                if (local.getDescription() != null && !local.getDescription().isEmpty()) {
                    return local.getDescription();
                }
                return String.format("%s, %s - %s",
                    local.getStreet(),
                    local.getNeighborhood(),
                    local.getCity());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
