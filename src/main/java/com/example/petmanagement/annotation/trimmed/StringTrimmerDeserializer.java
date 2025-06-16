package com.example.petmanagement.annotation.trimmed;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StringTrimmerDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        if (value == null) {
            return null;
        }

        try {
            Object currentValue = parser.currentValue();
            String fieldName = parser.currentName();
            java.lang.reflect.Field field = currentValue.getClass().getDeclaredField(fieldName);
            if (field.isAnnotationPresent(Trimmed.class)) {
                return value.trim();
            }
        } catch (NoSuchFieldException e) {
            // If field is not found, return original value
        }

        return value;
    }
}