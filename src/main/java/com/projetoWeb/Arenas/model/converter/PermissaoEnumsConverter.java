package com.projetoWeb.Arenas.model.converter;

import com.projetoWeb.Arenas.model.enums.PermissaoEnums;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PermissaoEnumsConverter implements AttributeConverter<PermissaoEnums, String> {

    @Override
    public String convertToDatabaseColumn(PermissaoEnums attribute) {
        if (attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public PermissaoEnums convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (PermissaoEnums e : PermissaoEnums.values()) {
            if (e.getValue().equals(dbData)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Valor inválido para PermissaoEnums: " + dbData);
    }
}