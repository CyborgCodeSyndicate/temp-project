package com.theairebellion.zeus.db.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.theairebellion.zeus.db.exceptions.JsonPathExtractionException;

/**
 * Utility class for extracting values from JSON using JsonPath.
 *
 * <p>This class facilitates extracting specific values from JSON structures
 * by applying JsonPath expressions and converting the results to the desired type.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class JsonPathExtractor {
   private final ObjectMapper objectMapper;
   private final Configuration jsonPathConfig;

   /**
    * Constructs a {@code JsonPathExtractor} with a custom JsonPath configuration.
    *
    * @param objectMapper   The {@link ObjectMapper} for JSON processing.
    * @param jsonPathConfig The {@link Configuration} for JsonPath evaluation.
    */
   public JsonPathExtractor(ObjectMapper objectMapper, Configuration jsonPathConfig) {
      this.objectMapper = objectMapper.copy();
      this.jsonPathConfig = jsonPathConfig;
   }

   /**
    * Constructs a {@code JsonPathExtractor} with a default JsonPath configuration.
    *
    * <p>The default configuration enables {@code SUPPRESS_EXCEPTIONS} option.
    *
    * @param objectMapper The {@link ObjectMapper} for JSON processing.
    */
   public JsonPathExtractor(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper.copy();
      this.jsonPathConfig = Configuration.builder()
            .options(Option.SUPPRESS_EXCEPTIONS)
            .build();
   }

   /**
    * Extracts a value from the given data using the specified JsonPath expression.
    *
    * @param <T>        The expected type of the extracted value.
    * @param data       The data object containing JSON structure.
    * @param jsonPath   The JsonPath expression to apply.
    * @param resultType The target type of the extracted value.
    * @return The extracted value converted to the specified type, or {@code null} if not found.
    * @throws JsonPathExtractionException If JSON conversion or extraction fails.
    */
   public <T> T extract(Object data, String jsonPath, Class<T> resultType) {
      String json = convertToJson(data);
      Object extracted = applyJsonPath(json, jsonPath);

      if (extracted == null) {
         return null;
      }

      return convertValue(extracted, resultType);
   }

   /**
    * Converts an object to its JSON representation.
    *
    * @param data The object to convert.
    * @return The JSON string representation of the object.
    * @throws JsonPathExtractionException If the conversion fails.
    */
   private String convertToJson(Object data) {
      try {
         return objectMapper.writeValueAsString(data);
      } catch (Exception e) {
         throw new JsonPathExtractionException("Failed to convert object to JSON", e);
      }
   }

   /**
    * Applies a JsonPath expression to extract a value from the given JSON string.
    *
    * @param json     The JSON string to parse.
    * @param jsonPath The JsonPath expression to apply.
    * @return The extracted value, or {@code null} if not found.
    * @throws JsonPathExtractionException If JsonPath evaluation fails.
    */
   private Object applyJsonPath(String json, String jsonPath) {
      try {
         return JsonPath.using(jsonPathConfig)
               .parse(json)
               .read(jsonPath);
      } catch (Exception e) {
         throw new JsonPathExtractionException("Failed to apply JsonPath: " + jsonPath, e);
      }
   }

   /**
    * Converts the extracted value to the specified type.
    *
    * @param <T>        The expected type of the converted value.
    * @param extracted  The extracted JSON value.
    * @param resultType The target type.
    * @return The extracted value converted to the specified type.
    * @throws JsonPathExtractionException If the conversion fails.
    */
   private <T> T convertValue(Object extracted, Class<T> resultType) {
      try {
         return objectMapper.convertValue(extracted, resultType);
      } catch (Exception e) {
         throw new JsonPathExtractionException("Failed to convert extracted JSON to " + resultType.getSimpleName(), e);
      }
   }

}
