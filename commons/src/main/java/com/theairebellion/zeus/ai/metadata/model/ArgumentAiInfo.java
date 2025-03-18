package com.theairebellion.zeus.ai.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ArgumentAiInfo {

    private Class<?> type;
    private String name;
    private CreationType creationType;
    private boolean useAsKeyInStorage;
    private String description;
    private List<String> availableOptions;
    private List<AiFieldInfo> fieldsInfo;

}