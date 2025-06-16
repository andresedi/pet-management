package com.example.petmanagement.annotation.trimmed;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class TrimmedAnnotationHandler extends SimpleModule {
    public TrimmedAnnotationHandler() {
        super("trimmer");
        addDeserializer(String.class, new StringTrimmerDeserializer());
    }
}
