package com.theairebellion.zeus.db.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.theairebellion.zeus.db.exceptions.JsonPathExtractionException;

public class JsonPathExtractor {
    private final ObjectMapper objectMapper;
    private final Configuration jsonPathConfig;

    public JsonPathExtractor(ObjectMapper objectMapper, Configuration jsonPathConfig) {
        this.objectMapper = objectMapper;
        this.jsonPathConfig = jsonPathConfig;
    }

    public JsonPathExtractor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jsonPathConfig = Configuration.builder()
                .options(Option.SUPPRESS_EXCEPTIONS)
                .build();
    }

    public <T> T extract(Object data, String jsonPath, Class<T> resultType) {
        String json = convertToJson(data);
        Object extracted = applyJsonPath(json, jsonPath);

        if (extracted == null) {
            return null;
        }

        return convertValue(extracted, resultType);
    }

    private String convertToJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new JsonPathExtractionException("Failed to convert object to JSON", e);
        }
    }

    private Object applyJsonPath(String json, String jsonPath) {
        try {
            return JsonPath.using(jsonPathConfig)
                    .parse(json)
                    .read(jsonPath);
        } catch (Exception e) {
            throw new JsonPathExtractionException("Failed to apply JsonPath: " + jsonPath, e);
        }
    }

    private <T> T convertValue(Object extracted, Class<T> resultType) {
        try {
            return objectMapper.convertValue(extracted, resultType);
        } catch (Exception e) {
            throw new JsonPathExtractionException("Failed to convert extracted JSON to " + resultType.getSimpleName(), e);
        }
    }

}
