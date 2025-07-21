package com.axcent.TimeSheet.entities.customHelper;

import com.axcent.TimeSheet.entities.enums.Motivo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class  MotivoDeserializer extends JsonDeserializer<Motivo> {

    @Override
    public Motivo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return Motivo.valueOf(value.trim().toUpperCase()); // case-insensitive
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
