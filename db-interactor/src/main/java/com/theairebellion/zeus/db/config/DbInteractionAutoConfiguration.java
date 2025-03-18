package com.theairebellion.zeus.db.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures database interaction components.
 * <p>
 * This class defines essential beans for handling JSON-based database interactions.
 * It provides an {@link ObjectMapper} for JSON processing and a {@link JsonPathExtractor}
 * for extracting values from JSON responses.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@Configuration
public class DbInteractionAutoConfiguration {

    /**
     * Provides a default {@link ObjectMapper} instance for JSON processing.
     *
     * @return A singleton {@code ObjectMapper} bean.
     */
    @Bean
    public ObjectMapper defaultObjectMapper() {
        return new ObjectMapper();
    }

    /**
     * Configures a {@link JsonPathExtractor} bean for extracting values from JSON-based query responses.
     *
     * @param objectMapper The {@code ObjectMapper} used for JSON processing.
     * @return A {@code JsonPathExtractor} instance.
     */
    @Bean
    public JsonPathExtractor jsonPathExtractor(ObjectMapper objectMapper) {
        return new JsonPathExtractor(objectMapper);
    }
}
