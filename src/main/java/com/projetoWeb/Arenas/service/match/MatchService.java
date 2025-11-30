package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.projetoWeb.Arenas.model.UserMatch;
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

    /**
     * Finaliza uma partida alterando seu status para FINALIZADA.
     * Apenas o criador da partida pode finalizar.
     * Não permite finalizar partidas já canceladas ou finalizadas.
     * 
     * @param id ID da partida
     * @param matchDto DTO com ID do usuário criador
     * @return Match atualizada com status FINALIZADA
     * @throws EntityNotExistsException se a partida não existir ou usuário não for o criador
     * @throws IllegalStateException se a partida já estiver cancelada ou finalizada
     */
    public Match finalize(Long id, UserMatchDto matchDto) {
        User user = userService.getUserById(matchDto.creatorUserId());
        Match match = findById(id);

        // Valida se o usuário é o criador da partida
        if (!user.getId().equals(match.getCreaterUserId().getId())) {
            throw new EntityNotExistsException("Apenas o criador da partida pode finalizá-la");
        }

        // Valida se a partida já está cancelada
        if (match.getMatchStatus() == MatchStatus.CANCELADA) {
            throw new IllegalStateException("Não é possível finalizar uma partida cancelada");
        }

        // Valida se a partida já está finalizada
        if (match.getMatchStatus() == MatchStatus.FINALIZADA) {
            throw new IllegalStateException("Esta partida já está finalizada");
        }

        Match newMatch = Match.builder()
                .id(match.getId())
                .matchDate(match.getMatchDate())
                .title(match.getTitle())
                .maxPlayers(match.getMaxPlayers())
                .description(match.getDescription())
                .createrUserId(match.getCreaterUserId())
                .matchStatus(MatchStatus.FINALIZADA)
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
                .map(match -> convertToCalendarioDto(match, userId))
                .collect(Collectors.toList());
    }

    private CalendarioMatchDto convertToCalendarioDto(Match match, Long userId) {
        String localInfo = getLocalInfo(match);

        long participantesAtuais = userMatchRepository.countByMatch(match);
        UserMatch userMatch = userMatchRepository.findByMatch_IdAndUser_Id(match.getId(), userId);
        String participantesInfo = participantesAtuais + "/" + match.getMaxPlayers();

        return CalendarioMatchDto.builder()
                .id(match.getId())
                .titulo(match.getTitle())
                .dataHora(match.getMatchDate())
                .status(userMatch.getUserMatchStatus().getValue())
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

    /**
     * Busca partidas onde o usuário está vinculado como jogador através de UserMatch.
     * 
     * IMPORTANTE: Retorna APENAS partidas onde existe um registro em UserMatch
     * com userMatchStatus = CONFIRMADO para o userId especificado.
     * NÃO retorna partidas apenas porque o usuário é o criador.
     * 
     * @param userId ID do usuário que deve estar vinculado como jogador
     * @param status Status da partida a ser filtrado
     * @return Lista de partidas onde o usuário participa como jogador confirmado
     */
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

    /**
     * Busca todas as partidas criadas por um usuário específico
     * 
     * @param userId ID do usuário criador
     * @return Lista de partidas criadas pelo usuário
     */
    @Transactional(readOnly = true)
    public List<Match> findCreatedByUser(Long userId) {
        return matchRepository.findByCreaterUserId_IdAndMatchStatus(userId, MatchStatus.CONFIRMADA);
    }

    /**
     * Busca detalhes de uma partida incluindo jogadores e suas posições
     * 
     * @param matchId ID da partida
     * @return Match com detalhes completos
     */
    @Transactional(readOnly = true)
    public Match getMatchWithPlayers(Long matchId) {
        Match match = findById(matchId);
        // Os jogadores serão buscados via UserMatchRepository no controller
        return match;
    }

    /**
     * Atualiza as posições dos jogadores em uma partida
     * Valida se o mesmo jogador não está em múltiplas posições
     * 
     * @param matchId ID da partida
     * @param userId ID do usuário que está fazendo a alteração (deve ser o criador)
     * @param positions Mapa de posição -> userId
     * @throws EntityNotExistsException se a partida não existir ou usuário não for o criador
     * @throws IllegalArgumentException se houver jogador duplicado em posições
     */
    @Transactional
    public void updatePlayerPositions(Long matchId, Long userId, java.util.Map<String, Long> positions) {
        Match match = findById(matchId);
        
        // Valida se o usuário é o criador da partida
        if (!match.getCreaterUserId().getId().equals(userId)) {
            throw new EntityNotExistsException("Apenas o criador da partida pode gerenciar as posições");
        }
        
        // Valida se a partida não está finalizada
        if (match.getMatchStatus() == MatchStatus.FINALIZADA) {
            throw new IllegalStateException("Não é possível alterar posições de uma partida finalizada");
        }
        
        // Valida se não há jogadores duplicados em diferentes posições
        java.util.Set<Long> playerIds = new java.util.HashSet<>();
        for (java.util.Map.Entry<String, Long> entry : positions.entrySet()) {
            Long playerId = entry.getValue();
            if (playerId != null) {
                if (playerIds.contains(playerId)) {
                    throw new IllegalArgumentException("O mesmo jogador não pode ocupar múltiplas posições");
                }
                playerIds.add(playerId);
            }
        }
        
        // Busca todos os UserMatch da partida
        List<com.projetoWeb.Arenas.model.UserMatch> userMatches = userMatchRepository.findByMatchId(matchId);
        
        // Atualiza as posições
        for (com.projetoWeb.Arenas.model.UserMatch userMatch : userMatches) {
            Long userMatchUserId = userMatch.getUser().getId();
            
            // Procura qual posição foi atribuída a este jogador
            String assignedPosition = null;
            for (java.util.Map.Entry<String, Long> entry : positions.entrySet()) {
                if (entry.getValue() != null && entry.getValue().equals(userMatchUserId)) {
                    assignedPosition = entry.getKey();
                    break;
                }
            }
            
            // Atualiza a posição do jogador
            if (assignedPosition != null) {
                try {
                    com.projetoWeb.Arenas.model.enums.RolePlayer rolePlayer = 
                        mapPositionToRolePlayer(assignedPosition);
                    userMatch.setRolePlayer(rolePlayer);
                    userMatchRepository.save(userMatch);
                } catch (IllegalArgumentException e) {
                    // Posição inválida, ignora
                }
            }
        }
    }

    /**
     * Mapeia a posição do frontend para o enum RolePlayer
     */
    private com.projetoWeb.Arenas.model.enums.RolePlayer mapPositionToRolePlayer(String position) {
        return switch (position) {
            case "GOL" -> com.projetoWeb.Arenas.model.enums.RolePlayer.GOLEIRO;
            case "LD" -> com.projetoWeb.Arenas.model.enums.RolePlayer.LATERAL_DIREITO;
            case "LE" -> com.projetoWeb.Arenas.model.enums.RolePlayer.LATERAL_ESQUERDO;
            case "ZAG1", "ZAG2" -> com.projetoWeb.Arenas.model.enums.RolePlayer.ZAGUEIRO;
            case "VOL" -> com.projetoWeb.Arenas.model.enums.RolePlayer.VOLANTE;
            case "MC1", "MC2" -> com.projetoWeb.Arenas.model.enums.RolePlayer.MEIA;
            case "ATA1", "ATA2", "ATA3" -> com.projetoWeb.Arenas.model.enums.RolePlayer.ATACANTE;
            default -> throw new IllegalArgumentException("Posição inválida: " + position);
        };
    }
}
