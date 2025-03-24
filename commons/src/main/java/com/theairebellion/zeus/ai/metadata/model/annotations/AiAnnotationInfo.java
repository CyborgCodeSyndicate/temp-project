package com.theairebellion.zeus.ai.metadata.model.annotations;

import com.theairebellion.zeus.ai.metadata.model.classes.Usage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiAnnotationInfo {

    private String annotationName;
    private String description;
    private List<Usage> usage;
    private List<String> targets;
    private String retentionPolicy;
    private boolean repeatable;
    private List<AiAnnotationFieldInfo> fields;
    private List<AiAnnotationInfo> nestedAnnotations;

}
