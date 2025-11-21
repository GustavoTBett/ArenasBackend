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
                .build();

        if(userMatchDto.userMatchStatus() != null) {
            if(!userMatchDto.userMatchStatus().isBlank()) {
                newUserMatch.setUserMatchStatus(UserMatchStatus.valueOf(userMatchDto.userMatchStatus()));
            }
        }

        return userMatchRepository.save(newUserMatch);
    }

    public UserMatch update(Long userMatchId, PatchUserMatchDto userMatchDto) {
        if((userMatchDto.userMatchStatus().isBlank())&&(userMatchDto.rolePlayer().isBlank())) {
            throw new EntityNotExistsException("Posicao do jogador ou status da partida devem ser preenchidos");
        }

        UserMatch searchedUsermatch = findById(userMatchId);

        UserMatch newUserMatch = UserMatch.builder()
                .id(searchedUsermatch.getId())
                .match(searchedUsermatch.getMatch())
                .user(searchedUsermatch.getUser())
                .build();

        if(!userMatchDto.userMatchStatus().isBlank()) {
            newUserMatch.setUserMatchStatus(UserMatchStatus.valueOf(userMatchDto.userMatchStatus()));
        }

        if(!userMatchDto.rolePlayer().isBlank()) {
            newUserMatch.setRolePlayer(RolePlayer.valueOf(userMatchDto.rolePlayer()));
        }

        return  userMatchRepository.save(newUserMatch);
    }
}
