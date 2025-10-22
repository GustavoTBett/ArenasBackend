package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoWeb.Arenas.model.UserMatch;
import com.projetoWeb.Arenas.model.enums.UserMatchStatus;
import com.projetoWeb.Arenas.repository.UserMatchRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;

@Service
public class UserMatchService {

  @Autowired
  private UserMatchRepository userMatchRepository;

  public List<UserMatch> findAll() {
    return userMatchRepository.findAll();
  }

  public UserMatch findById(Long id) {
    Optional<UserMatch> userMatch = userMatchRepository.findById(id);
    if (userMatch.isPresent()) {
      return userMatch.get();
    }
    throw new EntityNotExistsException("UserMatch Not Found");
  }

  public List<UserMatch> findByMatchId(Long matchId) {
    return userMatchRepository.findByMatchId(matchId);
  }

  public List<UserMatch> findByMatchIdAndUserMatchStatus(Long matchId, UserMatchStatus status) {
    return userMatchRepository.findByMatchIdAndUserMatchStatus(matchId, status);
  }

  public UserMatch save(UserMatch userMatch) {
    return userMatchRepository.save(userMatch);
  }

  public void deleteById(Long id) {
    userMatchRepository.deleteById(id);
  }

  public UserMatch update(UserMatch userMatch) {
    return userMatchRepository.save(userMatch);
  }

  public void deleteByMatchId(Long matchId) {
    deleteByMatchId(matchId);
  }

  public Long countByMatchId(Long matchId) {
    return Long.valueOf(userMatchRepository.findByMatchId(matchId).size());
  }

  public Long countByMatchIdAndUserMatchStatus(Long matchId, UserMatchStatus status) {
    return Long.valueOf(userMatchRepository.findByMatchIdAndUserMatchStatus(matchId, status).size());
  }

  public UserMatch findByUserIdAndMatchId(Long userId, Long matchId) {
    return userMatchRepository.findByUserIdAndMatchId(userId, matchId);
  }

}
