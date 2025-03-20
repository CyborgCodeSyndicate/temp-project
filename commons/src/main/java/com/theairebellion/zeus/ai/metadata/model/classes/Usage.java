package com.theairebellion.zeus.ai.metadata.model.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usage {

    private UsageLevel usageLevel;
    private String description;
    private String example;


}
