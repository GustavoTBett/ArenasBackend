package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.projetoWeb.Arenas.controller.match.dto.CalendarioMatchDto;
import com.projetoWeb.Arenas.repository.LocalMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetoWeb.Arenas.controller.match.dto.UserMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.MatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.model.LocalMatch;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import com.projetoWeb.Arenas.repository.MatchRepository;
import com.projetoWeb.Arenas.repository.UserMatchRepository;
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

    @Autowired
    private UserMatchRepository userMatchRepository;

    @Autowired
    private LocalMatchRepository localMatchRepository;

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
                .createrUserId(user)
                .matchStatus(MatchStatus.CONFIRMADA)
                .build();
        Match savedMatch = matchRepository.save(match);

        matchParameterService.create(savedMatch, matchDto.matchParameterDto());
        localMatchService.create(savedMatch, matchDto.localMatchDto());

        return savedMatch;
    }

    public Match update(Long id, MatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());
        Match searchedMatch = findById(id);

        if(user.getId() != searchedMatch.getCreaterUserId().getId()) {
            throw new EntityNotExistsException("Mis-matched user");
        }

        Match match = Match.builder()
                .id(id)
                .matchDate(matchDto.matchData())
                .title(matchDto.text())
                .maxPlayers(matchDto.maxPlayers())
                .description(matchDto.description())
                .createrUserId(user)
                .matchStatus(searchedMatch.getMatchStatus())
                .build();
        Match savedMatch = matchRepository.save(match);

        matchParameterService.updateByMatchId(savedMatch, matchDto.matchParameterDto());
        localMatchService.updateByMatchId(savedMatch, matchDto.localMatchDto());

        return savedMatch;
    }

    public Match cancel(Long id, UserMatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());
        Match match = findById(id);

        if(user.getId() != match.getCreaterUserId().getId()) {
            throw new EntityNotExistsException("Mis-matched user");
        }

        Match newMatch = Match.builder()
                .id(match.getId())
                .matchDate(match.getMatchDate())
                .title(match.getTitle())
                .maxPlayers(match.getMaxPlayers())
                .description(match.getDescription())
                .createrUserId(match.getCreaterUserId())
                .matchStatus(MatchStatus.CANCELADA)
                .build();

        return matchRepository.save(newMatch);
    }

    public void delete(Long id, UserMatchDto matchDto){
        User user = userService.getUserById(matchDto.creatorUserId());
        Match match = findById(id);

        if(user.getId() != match.getCreaterUserId().getId()) {
            throw new EntityNotExistsException("Mis-matched user");
        }

        matchRepository.deleteById(id);
        matchParameterService.deleteByMatchId(id);
        localMatchService.deleteByMatchId(id);
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

    public List<Match> findByStatus(MatchStatus status) {
        return matchRepository.findByMatchStatus(status);
    }

    public List<Match> findByUserAndMatchStatus(Long userId, MatchStatus status) {
        return matchRepository.findByUserAndMatchStatus(userId, status);
    }
}
