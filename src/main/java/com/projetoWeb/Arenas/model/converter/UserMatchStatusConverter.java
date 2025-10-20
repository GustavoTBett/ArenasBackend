package com.projetoWeb.Arenas.model.converter;

import com.projetoWeb.Arenas.model.enums.UserMatchStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserMatchStatusConverter implements AttributeConverter<UserMatchStatus, String> {

  @Override
  public String convertToDatabaseColumn(UserMatchStatus status) {
    if (status == null) {
      return null;
    }
    return status.getValue();
  }

  @Override
  public UserMatchStatus convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return null;
    }

    for (UserMatchStatus status : UserMatchStatus.values()) {
      if (status.getValue().equals(dbData)) {
        return status;
      }
    }

    throw new IllegalArgumentException("Valor inválido para UserMatchStatus: " + dbData);
  }

}
