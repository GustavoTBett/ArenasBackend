package com.projetoWeb.Arenas.controller.match.dto;

import com.projetoWeb.Arenas.model.enums.RolePlayer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SolicitacaoMatchDto {
    private Long userMatchId;
    private Long matchId;
    private String matchTitle;
    private LocalDateTime matchDate;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userProfilePic; // Base64
    private RolePlayer rolePlayer;
    private LocalDateTime solicitacaoData;
}
