package com.theairebellion.zeus.ai.metadata.model.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiMethodInfo {

    private Level level;
    private String name;
    private String description;
    private Class<?> returnClass;
    private List<Usage> usage;
    private List<ArgumentAiInfo> argumentsInfo;

}
