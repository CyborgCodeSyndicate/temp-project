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
public class AiClassInfo {

    private Class<?> type;
    private String description;
    private List<AiMethodInfo> methodsInfo;

}
