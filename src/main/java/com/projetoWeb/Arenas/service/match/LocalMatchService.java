package com.projetoWeb.Arenas.service.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetoWeb.Arenas.controller.match.dto.LocalMatchDto;
import com.projetoWeb.Arenas.model.LocalMatch;
import com.projetoWeb.Arenas.model.Match;
import com.projetoWeb.Arenas.repository.LocalMatchRepository;
import com.projetoWeb.Arenas.service.exception.EntityNotExistsException;

@Service
public class LocalMatchService {

    @Autowired
    private LocalMatchRepository localMatchRepository;

    public List<LocalMatch> findAll() {
        return localMatchRepository.findAll();
    }

    public LocalMatch findById(long id) {
        Optional<LocalMatch> localMatch = localMatchRepository.findById(id);
        if (localMatch.isPresent()) {
            return localMatch.get();
        }

        throw new EntityNotExistsException("Match Parameter Not Found");
    }

    public LocalMatch findByMatchId(long id) {
        Optional<LocalMatch> localMatch = localMatchRepository.findByMatch_Id(id);
        if (localMatch.isPresent()) {
            return localMatch.get();
        }

        throw new EntityNotExistsException("Match Parameter Not Found");
    }

    public LocalMatch create(Match match, LocalMatchDto localMatchDto){
        LocalMatch localMatch = LocalMatch.builder()
                .description(localMatchDto.localName())
                .zipCode(localMatchDto.localZipCode())
                .street(localMatchDto.localStreet())
                .number(localMatchDto.localNumber())
                .complement(localMatchDto.localComplement())
                .city(localMatchDto.localCity())
                .state(localMatchDto.localState())
                .neighborhood(localMatchDto.localNeighborhood())
                .match(match)
                .build();

        return localMatchRepository.save(localMatch);
    }

    public LocalMatch updateByMatchId(Match match, LocalMatchDto localMatchDto){
        LocalMatch savedLocalMatch = findByMatchId(match.getId());

        LocalMatch localMatch = LocalMatch.builder()
                .id(savedLocalMatch.getId())
                .description(localMatchDto.localName())
                .zipCode(localMatchDto.localZipCode())
                .street(localMatchDto.localStreet())
                .complement(localMatchDto.localComplement())
                .city(localMatchDto.localCity())
                .state(localMatchDto.localState())
                .neighborhood(localMatchDto.localNeighborhood())
                .match(match)
                .build();

        return localMatchRepository.save(localMatch);
    }

    public void deleteByMatchId(Long matchId){
        LocalMatch savedLocalMatch = findByMatchId(matchId);

        localMatchRepository.deleteById(savedLocalMatch.getId());
    }
}
