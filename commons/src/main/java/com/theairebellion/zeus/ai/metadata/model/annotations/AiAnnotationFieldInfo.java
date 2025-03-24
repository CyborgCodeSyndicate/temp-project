package com.theairebellion.zeus.ai.metadata.model.annotations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiAnnotationFieldInfo {

    private String name;
    private String type;
    private String description;
    private List<String> availableOptions;

}
