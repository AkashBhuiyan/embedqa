package com.akash.embedqa.converter;

import com.akash.embedqa.model.dtos.request.EnvironmentVariableDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: akash
 * Date: 17/12/25
 */
@Slf4j
@Converter(autoApply = false)
public class EnvironmentVariablesConverter
        implements AttributeConverter<List<EnvironmentVariableDTO>, String> {


    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String convertToDatabaseColumn(List<EnvironmentVariableDTO> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize environment variables", e);
            return "[]";
        }
    }


    @Override
    public List<EnvironmentVariableDTO> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(dbData,
                    new TypeReference<List<EnvironmentVariableDTO>>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize environment variables", e);
            return new ArrayList<>();
        }
    }
}
