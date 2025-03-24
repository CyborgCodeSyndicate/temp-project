package com.theairebellion.zeus.ai.metadata.model.classes;

import com.theairebellion.zeus.ai.metadata.model.annotations.AiAnnotationInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiUsage {

    private List<AiClassInfo> aiClassInfo;
    private List<AiAnnotationInfo> aiAnnotationInfos;


}
