package com.projetoWeb.Arenas.model.converter;

import com.projetoWeb.Arenas.model.enums.PermissaoEnums;
import com.projetoWeb.Arenas.model.enums.RolePlayer;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RolePlayerConverter implements AttributeConverter<RolePlayer, String> {

    @Override
    public String convertToDatabaseColumn(RolePlayer attribute) {
        if (attribute == null) return null;
        return attribute.getNome();
    }

    @Override
    public RolePlayer convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (RolePlayer e : RolePlayer.values()) {
            if (e.getNome().equals(dbData)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Valor inválido para RolePlayer: " + dbData);
    }
}