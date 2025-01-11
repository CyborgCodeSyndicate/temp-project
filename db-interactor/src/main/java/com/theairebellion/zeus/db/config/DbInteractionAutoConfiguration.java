package com.theairebellion.zeus.db.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theairebellion.zeus.db.json.JsonPathExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbInteractionAutoConfiguration {


    @Bean
    public ObjectMapper defaultObjectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public JsonPathExtractor jsonPathExtractor(ObjectMapper objectMapper) {
        return new JsonPathExtractor(objectMapper);
    }

}
