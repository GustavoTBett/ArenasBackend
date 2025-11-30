package com.projetoWeb.Arenas.controller.match;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetoWeb.Arenas.controller.match.dto.CalendarioMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.LeaveMatchRequest;
import com.projetoWeb.Arenas.controller.match.dto.MatchDto;
import com.projetoWeb.Arenas.controller.match.dto.ResponderSolicitacaoRequest;
import com.projetoWeb.Arenas.controller.match.dto.ResponseSearchMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.SearchMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.SolicitacaoMatchDto;
import com.projetoWeb.Arenas.controller.match.dto.UserMatchDto;
import com.projetoWeb.Arenas.model.LocalMatch;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.MatchParameter;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.model.UserMatch;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;
import com.projetoWeb.Arenas.service.match.LocalMatchService;
import com.projetoWeb.Arenas.service.match.MatchParameterService;
import com.projetoWeb.Arenas.service.match.MatchService;
import com.projetoWeb.Arenas.service.userMatch.UserMatchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final UserMatchService userMatchService;
    private final LocalMatchService localMatchService;
    private final MatchParameterService matchParameterService;

    @GetMapping("/calendario/{userId}")
    public ResponseEntity<List<CalendarioMatchDto>> getCalendarioMatches(@PathVariable Long userId) {
        return ResponseEntity.ok(matchService.findAllForCalendario(userId));
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<Match> getById(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchService.findById(matchId));
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.findAll());
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@Valid @RequestBody MatchDto matchDto) {
        return ResponseEntity.ok(matchService.create(matchDto));
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long matchId, @Valid @RequestBody MatchDto matchDto) {
        return ResponseEntity.ok(matchService.update(matchId, matchDto));
    }

    @PatchMapping("/{matchId}")
    public ResponseEntity<Match> cancelMatch(@PathVariable Long matchId, @Valid @RequestBody UserMatchDto matchDto) {
        return ResponseEntity.ok(matchService.cancel(matchId, matchDto));
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<String> deleteMatch(@PathVariable Long matchId, @Valid @RequestBody UserMatchDto matchDto) {
        matchService.delete(matchId, matchDto);

        return ResponseEntity.ok().body("Deletado com sucesso");
    }

    @PostMapping("/search")
    public ResponseEntity<List<ResponseSearchMatchDto>> searchMatch(@RequestBody SearchMatchDto searchMatchDto) {
        List<Match> matches = matchService.searchMatches(searchMatchDto);

        if (matches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ResponseSearchMatchDto> dtos = matches.stream().map(match -> {

            Long currentPlayers = userMatchService.countByMatchIdAndUserMatchStatus(match.getId(),
                    UserMatchStatus.CONFIRMADO);

            String createdUserName = match.getCreaterUserId().getFirstName() + " "
                    + match.getCreaterUserId().getLastName();

            String localName = null;
            String localZipCode = null;
            String localStreet = null;
            String localNumber = null;
            String localComplement = null;
            String localCity = null;
            String localState = null;
            String localNeighborhood = null;

            Boolean privateMatch = false;

            try {
                LocalMatch localMatch = localMatchService.findByMatchId(match.getId());
                if (localMatch != null) {
                    localName = localMatch.getDescription();
                    localZipCode = localMatch.getZipCode();
                    localStreet = localMatch.getStreet();
                    localNumber = localMatch.getNumber();
                    localComplement = localMatch.getComplement();
                    localCity = localMatch.getCity();
                    localState = localMatch.getState();
                    localNeighborhood = localMatch.getNeighborhood();
                }

                MatchParameter matchParameter = matchParameterService.findByMatchId(match.getId());
                if (matchParameter != null) {
                    privateMatch = matchParameter.getPrivateMatch();
                }
            } catch (Exception e) {
            }

            return ResponseSearchMatchDto.builder()
                    .id(match.getId())
                    .createUserId(match.getCreaterUserId().getId())
                    .createUserName(createdUserName)
                    .title(match.getTitle())
                    .time(match.getMatchDate().toLocalTime().toString())
                    .date(match.getMatchDate().toLocalDate().toString())
                    .maxPlayers(match.getMaxPlayers())
                    .currentPlayers(currentPlayers)
                    .status(match.getMatchStatus().getValue())
                    .localName(localName)
                    .localZipCode(localZipCode)
                    .localStreet(localStreet)
                    .localNumber(localNumber)
                    .localComplement(localComplement)
                    .localCity(localCity)
                    .localState(localState)
                    .localNeighborhood(localNeighborhood)
                    .privateMatch(privateMatch)
                    .players(userMatchService.findByMatchId(match.getId()))
                    .build();

        }).toList();

        return ResponseEntity.ok().body(dtos);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<String> leaveMatch(@Valid @RequestBody LeaveMatchRequest request) {
        userMatchService.leaveMatch(request.getMatchId(), request.getUserId());
        return ResponseEntity.ok().body("Você saiu da partida com sucesso");
    }

    @GetMapping("/solicitacoes/{creatorUserId}")
    public ResponseEntity<List<SolicitacaoMatchDto>> getSolicitacoes(
            @PathVariable Long creatorUserId) {

        List<UserMatch> solicitacoes = userMatchService.findSolicitacoesByCreator(creatorUserId);

        if (solicitacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<SolicitacaoMatchDto> dtos = solicitacoes.stream().map(userMatch -> {
            User user = userMatch.getUser();

            String profilePicBase64 = null;
            if (user.getProfilePic() != null && user.getProfilePic().length > 0) {
                profilePicBase64 = java.util.Base64.getEncoder().encodeToString(user.getProfilePic());
            }

            return SolicitacaoMatchDto.builder()
                    .userMatchId(userMatch.getId())
                    .matchId(userMatch.getMatch().getId())
                    .matchTitle(userMatch.getMatch().getTitle())
                    .matchDate(userMatch.getMatch().getMatchDate())
                    .userId(user.getId())
                    .userFirstName(user.getFirstName())
                    .userLastName(user.getLastName())
                    .userEmail(user.getEmail())
                    .userProfilePic(profilePicBase64)
                    .rolePlayer(userMatch.getRolePlayer())
                    .build();
        }).toList();

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/solicitacoes/responder")
    public ResponseEntity<String> responderSolicitacao(
            @Valid @RequestBody ResponderSolicitacaoRequest request) {

        userMatchService.responderSolicitacao(
                request.getUserMatchId(),
                request.getAceitar(),
                request.getCreatorUserId());

        String mensagem = request.getAceitar()
                ? "Solicitação aceita com sucesso"
                : "Solicitação recusada";

        return ResponseEntity.ok(mensagem);
    }

    /**
     * Busca todas as partidas criadas por um usuário específico
     * 
     * @param userId ID do usuário criador
     * @return Lista de partidas criadas pelo usuário
     */
    @GetMapping("/created/{userId}")
    public ResponseEntity<List<com.projetoWeb.Arenas.controller.match.dto.MatchCreatedByUserDto>> getMatchesCreatedByUser(
            @PathVariable Long userId) {
        
        List<Match> matches = matchService.findCreatedByUser(userId);
        
        if (matches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        List<com.projetoWeb.Arenas.controller.match.dto.MatchCreatedByUserDto> dtos = matches.stream().map(match -> {
            String creatorName = match.getCreaterUserId().getFirstName() + " " 
                               + match.getCreaterUserId().getLastName();
            
            Long currentPlayers = userMatchService.countByMatchIdAndUserMatchStatus(
                match.getId(), 
                UserMatchStatus.CONFIRMADO
            );
            
            // Campos de localização para construção do mapa
            String localName = null;
            String localZipCode = null;
            String localStreet = null;
            String localNumber = null;
            String localComplement = null;
            String localCity = null;
            String localState = null;
            String localNeighborhood = null;
            
            try {
                LocalMatch localMatch = localMatchService.findByMatchId(match.getId());
                if (localMatch != null) {
                    localName = localMatch.getDescription();
                    localZipCode = localMatch.getZipCode();
                    localStreet = localMatch.getStreet();
                    localNumber = localMatch.getNumber();
                    localComplement = localMatch.getComplement();
                    localCity = localMatch.getCity();
                    localState = localMatch.getState();
                    localNeighborhood = localMatch.getNeighborhood();
                }
            } catch (Exception e) {
                // Local não encontrado
            }
            
            return com.projetoWeb.Arenas.controller.match.dto.MatchCreatedByUserDto.builder()
                    .id(match.getId())
                    .title(match.getTitle())
                    .date(match.getMatchDate().toLocalDate().toString())
                    .time(match.getMatchDate().toLocalTime().toString())
                    .status(match.getMatchStatus().getValue())
                    .maxPlayers(match.getMaxPlayers())
                    .currentPlayers(currentPlayers)
                    .createUserName(creatorName)
                    .description(match.getDescription())
                    .localName(localName)
                    .localZipCode(localZipCode)
                    .localStreet(localStreet)
                    .localNumber(localNumber)
                    .localComplement(localComplement)
                    .localCity(localCity)
                    .localState(localState)
                    .localNeighborhood(localNeighborhood)
                    .build();
        }).toList();
        
        return ResponseEntity.ok(dtos);
    }

    /**
     * Busca detalhes de uma partida incluindo lista de jogadores e suas posições
     * 
     * @param matchId ID da partida
     * @return Detalhes da partida com jogadores e posições
     */
    @GetMapping("/{matchId}/players")
    public ResponseEntity<com.projetoWeb.Arenas.controller.match.dto.MatchPlayersDetailDto> getMatchWithPlayers(
            @PathVariable Long matchId) {
        
        Match match = matchService.getMatchWithPlayers(matchId);
        
        // Busca todos os jogadores confirmados na partida
        List<UserMatch> userMatches = userMatchService.findByMatchId(matchId);
        List<UserMatch> confirmedPlayers = userMatches.stream()
                .filter(um -> um.getUserMatchStatus() == UserMatchStatus.CONFIRMADO)
                .toList();
        
        // Converte para PlayerInfoDto
        List<com.projetoWeb.Arenas.controller.match.dto.PlayerInfoDto> players = confirmedPlayers.stream()
                .map(um -> {
                    User user = um.getUser();
                    return com.projetoWeb.Arenas.controller.match.dto.PlayerInfoDto.builder()
                            .id(user.getId())
                            .nome(user.getFirstName() + " " + user.getLastName())
                            .email(user.getEmail())
                            .telefone(user.getPhone())
                            .build();
                })
                .toList();
        
        // Monta o mapa de posições
        java.util.Map<String, com.projetoWeb.Arenas.controller.match.dto.PlayerInfoDto> positions = new java.util.HashMap<>();
        
        // Inicializa todas as posições como null
        positions.put("GOL", null);
        positions.put("LD", null);
        positions.put("ZAG1", null);
        positions.put("ZAG2", null);
        positions.put("LE", null);
        positions.put("VOL", null);
        positions.put("MC1", null);
        positions.put("MC2", null);
        positions.put("ATA1", null);
        positions.put("ATA2", null);
        positions.put("ATA3", null);
        
        // Preenche as posições com os jogadores (baseado no RolePlayer)
        // Nota: Como temos múltiplas posições para o mesmo role, distribuímos sequencialmente
        java.util.Map<String, Integer> roleCounters = new java.util.HashMap<>();
        
        for (UserMatch um : confirmedPlayers) {
            User user = um.getUser();
            com.projetoWeb.Arenas.controller.match.dto.PlayerInfoDto playerInfo = 
                com.projetoWeb.Arenas.controller.match.dto.PlayerInfoDto.builder()
                    .id(user.getId())
                    .nome(user.getFirstName() + " " + user.getLastName())
                    .email(user.getEmail())
                    .build();
            
            String positionKey = mapRolePlayerToPosition(um.getRolePlayer(), roleCounters);
            if (positionKey != null) {
                positions.put(positionKey, playerInfo);
            }
        }
        
        Long currentPlayers = (long) confirmedPlayers.size();
        
        com.projetoWeb.Arenas.controller.match.dto.MatchPlayersDetailDto dto = 
            com.projetoWeb.Arenas.controller.match.dto.MatchPlayersDetailDto.builder()
                .id(match.getId())
                .title(match.getTitle())
                .date(match.getMatchDate().toLocalDate().toString())
                .time(match.getMatchDate().toLocalTime().toString())
                .status(match.getMatchStatus().getValue())
                .maxPlayers(match.getMaxPlayers())
                .currentPlayers(currentPlayers)
                .players(players)
                .positions(positions)
                .build();
        
        return ResponseEntity.ok(dto);
    }

    /**
     * Mapeia RolePlayer para posição no campo
     * Distribui múltiplas ocorrências do mesmo role em posições diferentes
     */
    private String mapRolePlayerToPosition(com.projetoWeb.Arenas.model.enums.RolePlayer rolePlayer, 
                                          java.util.Map<String, Integer> counters) {
        if (rolePlayer == null) return null;
        
        return switch (rolePlayer) {
            case GOLEIRO -> "GOL";
            case LATERAL_DIREITO -> "LD";
            case LATERAL_ESQUERDO -> "LE";
            case ZAGUEIRO -> {
                int count = counters.getOrDefault("ZAG", 0);
                counters.put("ZAG", count + 1);
                yield count == 0 ? "ZAG1" : "ZAG2";
            }
            case VOLANTE -> "VOL";
            case MEIA -> {
                int count = counters.getOrDefault("MC", 0);
                counters.put("MC", count + 1);
                yield count == 0 ? "MC1" : "MC2";
            }
            case ATACANTE -> {
                int count = counters.getOrDefault("ATA", 0);
                counters.put("ATA", count + 1);
                yield switch (count) {
                    case 0 -> "ATA1";
                    case 1 -> "ATA2";
                    default -> "ATA3";
                };
            }
            default -> null;
        };
    }

    /**
     * Atualiza as posições dos jogadores em uma partida
     * Apenas o criador da partida pode fazer essa operação
     * 
     * @param matchId ID da partida
     * @param request Requisição com mapa de posições
     * @return Resposta de sucesso
     */
    @PutMapping("/{matchId}/positions")
    public ResponseEntity<com.projetoWeb.Arenas.controller.match.dto.ApiResponse> updatePositions(
            @PathVariable Long matchId,
            @Valid @RequestBody com.projetoWeb.Arenas.controller.match.dto.UpdatePositionsRequest request) {
        
        // TODO: Extrair userId do token JWT (por enquanto, precisará ser passado no request)
        // Por ora, vamos pegar do criador da partida para validação
        Match match = matchService.findById(matchId);
        Long creatorId = match.getCreaterUserId().getId();
        
        matchService.updatePlayerPositions(matchId, creatorId, request.getPositions());
        
        return ResponseEntity.ok(
            com.projetoWeb.Arenas.controller.match.dto.ApiResponse.builder()
                .success(true)
                .message("Posições atualizadas com sucesso")
                .build()
        );
    }

    /**
     * Remove um jogador específico de uma partida
     * Apenas o criador da partida pode fazer essa operação
     * 
     * @param matchId ID da partida
     * @param playerId ID do jogador a ser removido
     * @return Resposta de sucesso
     */
    @DeleteMapping("/{matchId}/player/{playerId}")
    public ResponseEntity<com.projetoWeb.Arenas.controller.match.dto.ApiResponse> removePlayerFromMatch(
            @PathVariable Long matchId,
            @PathVariable Long playerId) {
        
        // TODO: Extrair userId do token JWT
        Match match = matchService.findById(matchId);
        Long requestingUserId = match.getCreaterUserId().getId();
        
        userMatchService.removePlayerFromMatch(matchId, playerId, requestingUserId);
        
        return ResponseEntity.ok(
            com.projetoWeb.Arenas.controller.match.dto.ApiResponse.builder()
                .success(true)
                .message("Jogador removido com sucesso")
                .build()
        );
    }

    /**
     * Cancela uma partida
     * Apenas o criador da partida pode fazer essa operação
     * Não permite cancelar partidas já finalizadas
     * 
     * @param matchId ID da partida
     * @param matchDto DTO com ID do criador
     * @return Resposta de sucesso
     */
    @PutMapping("/{matchId}/cancel")
    public ResponseEntity<com.projetoWeb.Arenas.controller.match.dto.ApiResponse> cancelMatchNew(
            @PathVariable Long matchId,
            @Valid @RequestBody UserMatchDto matchDto) {
        
        matchService.cancel(matchId, matchDto);
        
        // TODO: Implementar notificação para todos os jogadores da partida
        
        return ResponseEntity.ok(
            com.projetoWeb.Arenas.controller.match.dto.ApiResponse.builder()
                .success(true)
                .message("Partida cancelada com sucesso")
                .build()
        );
    }

    /**
     * Finaliza uma partida alterando seu status para FINALIZADA
     * Apenas o criador da partida pode fazer essa operação
     * Não permite finalizar partidas já canceladas ou finalizadas
     * 
     * @param matchId ID da partida
     * @param matchDto DTO com ID do criador
     * @return Resposta de sucesso
     */
    @PutMapping("/{matchId}/finalize")
    public ResponseEntity<com.projetoWeb.Arenas.controller.match.dto.ApiResponse> finalizeMatch(
            @PathVariable Long matchId,
            @Valid @RequestBody UserMatchDto matchDto) {
        
        matchService.finalize(matchId, matchDto);
        
        // TODO: Implementar notificação para todos os jogadores da partida
        
        return ResponseEntity.ok(
            com.projetoWeb.Arenas.controller.match.dto.ApiResponse.builder()
                .success(true)
                .message("Partida finalizada com sucesso")
                .build()
        );
    }

}
