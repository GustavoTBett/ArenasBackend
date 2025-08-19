package com.projetoWeb.Arenas.controller.dto;

import com.projetoWeb.Arenas.model.enums.PermissaoEnums;
import lombok.Builder;

@Builder
public class ResponseUserDto {
    private String email;
    private String firstName;
    private PermissaoEnums role;
}
