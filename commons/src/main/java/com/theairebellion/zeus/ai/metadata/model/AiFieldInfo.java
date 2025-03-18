package com.theairebellion.zeus.ai.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiFieldInfo {

    private Class<?> type;
    private String name;
    private String description;
    private CreationType creationType;
    private List<String> availableEnumOptions;
    private List<Usage> usage;

}
