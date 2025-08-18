package com.projetoWeb.Arenas.model.converter;

import com.projetoWeb.Arenas.model.enums.MatchStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MatchStatusConverter implements AttributeConverter<MatchStatus, String> {

    @Override
    public String convertToDatabaseColumn(MatchStatus attribute) {
        if (attribute == null) return null;
        return attribute.getDescricao();
    }

    @Override
    public MatchStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (MatchStatus e : MatchStatus.values()) {
            if (e.getDescricao().equals(dbData)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Valor inválido para MatchStatus: " + dbData);
    }
}