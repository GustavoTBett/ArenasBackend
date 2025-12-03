package com.projetoWeb.Arenas.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Deserializer customizado para LocalDateTime que aceita tanto
 * strings com offset (ZonedDateTime) quanto sem offset (LocalDateTime).
 * Quando recebe um valor com offset, extrai apenas a parte local da data/hora.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            // Tenta parsear como LocalDateTime primeiro (sem offset)
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            try {
                // Se falhar, tenta como ZonedDateTime e extrai o LocalDateTime
                ZonedDateTime zdt = ZonedDateTime.parse(value);
                return zdt.toLocalDateTime();
            } catch (DateTimeParseException e2) {
                throw new IOException("Não foi possível parsear a data: " + value, e2);
            }
        }
    }
}
