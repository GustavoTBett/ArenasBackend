package com.projetoWeb.Arenas.service.userMatch;

import com.projetoWeb.Arenas.controller.userMatch.UserMatchController;
import com.projetoWeb.Arenas.controller.userMatch.dto.UserMatchDto;
import com.projetoWeb.Arenas.repository.UserMatchRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;
import com.projetoWeb.Arenas.service.match.MatchService;
import com.projetoWeb.Arenas.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
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

    private UserMatch findById(long id) {
        Optional<UserMatch> userMatch = userMatchRepository.findById(id);
        if (userMatch.isPresent()) {
           return userMatch.get();
        }
        throw  new EntityNotExistsException("User Match Not Found");
    }

    public List<UserMatch> findByMatchId(long id) {
        return userMatchRepository.findByMatchId(id);
    }

    public List<UserMatch> findByUserAndMatch(UserMatchDto userMatchDto) {
        return userMatchRepository.findByMatchIdAndUserId(userMatchDto.matchId(), userMatchDto.userId());
    }

    public UserMatch create(UserMatchDto userMatchDto) {

    }
}
