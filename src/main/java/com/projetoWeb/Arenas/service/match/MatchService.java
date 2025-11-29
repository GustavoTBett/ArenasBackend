package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projetoWeb.Arenas.controller.match.dto.CalendarioMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.MatchDto;
import com.projetoWeb.Arenas.controller.match.dto.SearchMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.UserMatchDto;
import com.projetoWeb.Arenas.model.LocalMatch;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.model.enums.MatchStatus;
import com.projetoWeb.Arenas.repository.LocalMatchRepository;
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

    public Match create(MatchDto matchDto) {
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

    public Match update(Long id, MatchDto matchDto) {
        User user = userService.getUserById(matchDto.creatorUserId());
        Match searchedMatch = findById(id);

        if (user.getId() != searchedMatch.getCreaterUserId().getId()) {
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

    public Match cancel(Long id, UserMatchDto matchDto) {
        User user = userService.getUserById(matchDto.creatorUserId());
        Match match = findById(id);

        if (user.getId() != match.getCreaterUserId().getId()) {
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

    public void delete(Long id, UserMatchDto matchDto) {
        User user = userService.getUserById(matchDto.creatorUserId());
        Match match = findById(id);

        if (user.getId() != match.getCreaterUserId().getId()) {
            throw new EntityNotExistsException("Mis-matched user");
        }

        matchRepository.deleteById(id);
        matchParameterService.deleteByMatchId(id);
        localMatchService.deleteByMatchId(id);
    }

    @Transactional(readOnly = true)
    public List<CalendarioMatchDto> findAllForCalendario(Long userId) {
        List<Match> matches = matchRepository.findAllByUserId(userId);

        return matches.stream()
                .map(this::convertToCalendarioDto)
                .collect(Collectors.toList());
    }

    private CalendarioMatchDto convertToCalendarioDto(Match match) {
        String localInfo = getLocalInfo(match);

        long participantesAtuais = userMatchRepository.countByMatch(match);
        String participantesInfo = participantesAtuais + "/" + match.getMaxPlayers();

        return CalendarioMatchDto.builder()
                .id(match.getId())
                .titulo(match.getTitle())
                .dataHora(match.getMatchDate())
                .status(match.getMatchStatus().getValue())
                .local(localInfo)
                .participantes(participantesInfo)
                .build();
    }

    private String getLocalInfo(Match match) {
        try {
            Optional<LocalMatch> localMatch = localMatchRepository.findByMatch(match);

            if (localMatch.isPresent()) {
                LocalMatch local = localMatch.get();

                if (hasValidAddress(local)) {
                    return formatAddress(local);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private boolean hasValidAddress(LocalMatch local) {
        return local.getStreet() != null && !local.getStreet().isEmpty() &&
                local.getCity() != null && !local.getCity().isEmpty();
    }

    private String formatAddress(LocalMatch local) {
        StringBuilder address = new StringBuilder();

        if (local.getStreet() != null && !local.getStreet().isEmpty()) {
            address.append(local.getStreet());
        }

        if (local.getNumber() != null && !local.getNumber().isEmpty()) {
            address.append(", ").append(local.getNumber());
        }

        if (local.getNeighborhood() != null && !local.getNeighborhood().isEmpty()) {
            address.append(" - ").append(local.getNeighborhood());
        }

        if (local.getCity() != null && !local.getCity().isEmpty()) {
            address.append(", ").append(local.getCity());
        }

        if (local.getState() != null && !local.getState().isEmpty()) {
            address.append(" - ").append(local.getState());
        }

        if (local.getZipCode() != null && !local.getZipCode().isEmpty()) {
            address.append(" (").append(local.getZipCode()).append(")");
        }

        return address.toString();
    }

    public List<Match> findByStatus(MatchStatus status) {
        return matchRepository.findByMatchStatus(status);
    }

    public List<Match> findByUserAndMatchStatus(Long userId, MatchStatus status) {
        return matchRepository.findByUserAndMatchStatus(userId, status);
    }

    public List<Match> searchMatches(SearchMatchDto searchMatchDto) {
        return matchRepository.searchMatches(
                searchMatchDto.title(),
                searchMatchDto.status(),
                searchMatchDto.localName(),
                searchMatchDto.city(),
                searchMatchDto.zipCode(),
                searchMatchDto.matchLevel(),
                searchMatchDto.date(),
                searchMatchDto.time(),
                searchMatchDto.userValue());
    }
}
