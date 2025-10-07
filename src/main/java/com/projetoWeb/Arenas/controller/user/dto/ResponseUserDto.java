package com.projetoWeb.Arenas.controller.user.dto;

import com.projetoWeb.Arenas.model.enums.PermissaoEnums;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserDto {
    private String email;
    private String firstName;
    private PermissaoEnums role;
}
