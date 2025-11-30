package com.projetoWeb.Arenas.service.userMatch;

import com.projetoWeb.Arenas.controller.userMatch.dto.CreateUserMatchDto;
import com.projetoWeb.Arenas.controller.userMatch.dto.PatchUserMatchDto;
import com.projetoWeb.Arenas.controller.userMatch.dto.SearchUserMatchDto;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.model.enums.RolePlayer;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;
import com.projetoWeb.Arenas.repository.UserMatchRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;
import com.projetoWeb.Arenas.service.match.MatchService;
import com.projetoWeb.Arenas.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projetoWeb.Arenas.model.UserMatch;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserMatchService {

    @Autowired
    private UserMatchRepository userMatchRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MatchService matchService;

    public UserMatch findById(long id) {
        Optional<UserMatch> userMatch = userMatchRepository.findById(id);
        if (userMatch.isPresent()) {
           return userMatch.get();
        }
        throw  new EntityNotExistsException("User Match Not Found");
    }

    public List<UserMatch> findByMatchId(long id) {
        return userMatchRepository.findByMatchId(id);
    }

    public List<UserMatch> findByUserAndMatch(SearchUserMatchDto userMatchDto) {
        return userMatchRepository.findByMatchIdAndUserId(userMatchDto.matchId(), userMatchDto.userId());
    }

    public void leaveMatch(Long matchId, Long userId) {
        List<UserMatch> userMatches = userMatchRepository.findByMatchId(matchId);

        UserMatch userMatch = userMatches.stream()
                .filter(um -> um.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new EntityNotExistsException("Você não está participando desta partida"));

        userMatchRepository.delete(userMatch);
    }

    public List<UserMatch> findSolicitacoesByCreator(Long creatorUserId) {
        return userMatchRepository.findAll().stream()
                .filter(um -> um.getMatch().getCreaterUserId().getId().equals(creatorUserId)
                        && um.getUserMatchStatus() == UserMatchStatus.SOLICITADO)
                .toList();
    }

    public void responderSolicitacao(Long userMatchId, Boolean aceitar, Long creatorUserId) {
        UserMatch userMatch = findById(userMatchId);

        if (!userMatch.getMatch().getCreaterUserId().getId().equals(creatorUserId)) {
            throw new EntityNotExistsException("Você não tem permissão para responder esta solicitação");
        }

        if (userMatch.getUserMatchStatus() != UserMatchStatus.SOLICITADO) {
            throw new EntityNotExistsException("Esta solicitação não está mais pendente");
        }

        if (aceitar) {
            long participantes = userMatchRepository.findByMatchIdAndUserMatchStatus(
                    userMatch.getMatch().getId(),
                    UserMatchStatus.CONFIRMADO
            ).size();

            if (participantes >= userMatch.getMatch().getMaxPlayers() - 1) {
                throw new EntityNotExistsException("Não há mais vagas disponíveis");
            }

            userMatch.setUserMatchStatus(UserMatchStatus.CONFIRMADO);
            userMatchRepository.save(userMatch);
        } else {
            userMatch.setUserMatchStatus(UserMatchStatus.RECUSADO);
            userMatchRepository.save(userMatch);
        }
    }

    public Long countByMatchId(Long matchId) {
        return Long.valueOf(userMatchRepository.findByMatchId(matchId).size());
    }

    public Long countByMatchIdAndUserMatchStatus(Long matchId, UserMatchStatus status) {
        return Long.valueOf(userMatchRepository.findByMatchIdAndUserMatchStatus(matchId, status).size());
    }

    public UserMatch create(CreateUserMatchDto userMatchDto) {
        User user = userService.getUserById(userMatchDto.userId());
        Match match = matchService.findById(userMatchDto.matchId());

        List<UserMatch> searchedMatches = findByUserAndMatch(new SearchUserMatchDto(userMatchDto.userId(), userMatchDto.matchId()));
        if(searchedMatches.stream().anyMatch(currentUserMatch -> currentUserMatch.getUserMatchStatus() != UserMatchStatus.CONFIRMADO)) {
           throw new EntityNotExistsException("Pedido de participacao ja existe");
        }

        UserMatch newUserMatch = UserMatch.builder()
                .match(match)
                .user(user)
                .rolePlayer(RolePlayer.fromString(userMatchDto.rolePlayer()))
                .userMatchStatus(UserMatchStatus.CONFIRMADO)
                .build();

        if(StringUtils.hasText(userMatchDto.userMatchStatus())) {
            newUserMatch.setUserMatchStatus(UserMatchStatus.fromString(userMatchDto.userMatchStatus()));
        }

        return userMatchRepository.save(newUserMatch);
    }

    public UserMatch update(Long userMatchId, PatchUserMatchDto userMatchDto) {
        if((!StringUtils.hasText(userMatchDto.userMatchStatus())) && (!StringUtils.hasText(userMatchDto.rolePlayer()))) {
            throw new EntityNotExistsException("Posicao do jogador ou status da partida devem ser preenchidos");
        }

        UserMatch searchedUsermatch = findById(userMatchId);

        UserMatch newUserMatch = UserMatch.builder()
                .id(searchedUsermatch.getId())
                .match(searchedUsermatch.getMatch())
                .user(searchedUsermatch.getUser())
                .userMatchStatus(searchedUsermatch.getUserMatchStatus())
                .rolePlayer(searchedUsermatch.getRolePlayer())
                .build();

        if (StringUtils.hasText(userMatchDto.userMatchStatus())) {
            newUserMatch.setUserMatchStatus(UserMatchStatus.fromString(userMatchDto.userMatchStatus()));
        }

        if(StringUtils.hasText(userMatchDto.rolePlayer())) {
            newUserMatch.setRolePlayer(RolePlayer.fromString(userMatchDto.rolePlayer()));
        }

        return  userMatchRepository.save(newUserMatch);
    }

    /**
     * Remove um jogador específico de uma partida
     * Apenas o criador da partida pode remover jogadores
     * Não permite remover jogadores de partidas finalizadas
     * 
     * @param matchId ID da partida
     * @param playerId ID do jogador a ser removido
     * @param requestingUserId ID do usuário que está fazendo a requisição
     * @throws EntityNotExistsException se a partida ou jogador não existir
     * @throws IllegalStateException se a partida já foi finalizada
     * @throws SecurityException se o usuário não for o criador da partida
     */
    public void removePlayerFromMatch(Long matchId, Long playerId, Long requestingUserId) {
        Match match = matchService.findById(matchId);
        
        // Valida se o usuário é o criador da partida
        if (!match.getCreaterUserId().getId().equals(requestingUserId)) {
            throw new SecurityException("Apenas o criador da partida pode remover jogadores");
        }
        
        // Valida se a partida não está finalizada
        if (match.getMatchStatus() == com.projetoWeb.Arenas.model.enums.MatchStatus.FINALIZADA) {
            throw new IllegalStateException("Não é possível remover jogadores de uma partida finalizada");
        }
        
        // Valida se não está tentando remover o criador
        if (match.getCreaterUserId().getId().equals(playerId)) {
            throw new IllegalArgumentException("O criador da partida não pode ser removido");
        }
        
        // Busca o UserMatch específico
        UserMatch userMatch = userMatchRepository.findByMatch_IdAndUser_Id(matchId, playerId);
        
        if (userMatch == null) {
            throw new EntityNotExistsException("Jogador não encontrado nesta partida");
        }
        
        // Remove o jogador
        userMatchRepository.delete(userMatch);
    }
}
