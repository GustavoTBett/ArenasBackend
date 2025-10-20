package com.projetoWeb.Arenas.controller.user.dto;

import com.projetoWeb.Arenas.model.enums.PermissaoEnums;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserDto {
    private Long id;
    private String email;
    private String firstName;
    private PermissaoEnums role;
}
