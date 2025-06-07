package com.implementation.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonHelper {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectWriter prettyWriter = mapper.writerWithDefaultPrettyPrinter();

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON: " + e.getMessage(), e);
        }
    }

    public static String toPrettyJson(Object obj) {
        try {
            return prettyWriter.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to pretty JSON: " + e.getMessage(), e);
        }
    }
}
