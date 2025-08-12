package com.projetoWeb.Arenas.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private Long expiresIn;
}
