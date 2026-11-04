package com.figure.figure.dto;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class StrictBooleanDeserializer extends ValueDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser parser, DeserializationContext context) {
        if (parser.currentToken() == JsonToken.VALUE_TRUE) return true;
        if (parser.currentToken() == JsonToken.VALUE_FALSE) return false;
        return context.reportInputMismatch(Boolean.class, "JSON boolean 값만 허용합니다.");
    }
}
