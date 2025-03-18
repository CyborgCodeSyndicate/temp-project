package com.theairebellion.zeus.ai.metadata.extractor;

import com.theairebellion.zeus.ai.metadata.model.AiFieldInfo;
import com.theairebellion.zeus.ai.metadata.model.ArgumentAiInfo;
import com.theairebellion.zeus.ai.metadata.model.CreationType;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentInfoExtractor {

    private final FieldInfoExtractor fieldInfoExtractor;


    public ArgumentInfoExtractor(FieldInfoExtractor fieldInfoExtractor) {
        this.fieldInfoExtractor = fieldInfoExtractor;
    }


    public List<ArgumentAiInfo> extractArgumentsInfo(Method method) {
        return Arrays.stream(method.getParameters())
                   .map(this::createArgumentInfo)
                   .collect(Collectors.toList());
    }


    private ArgumentAiInfo createArgumentInfo(Parameter parameter) {
        ArgumentAiInfo argumentAiInfo = new ArgumentAiInfo();
        Class<?> parameterType = parameter.getType();
        argumentAiInfo.setType(parameterType);
        argumentAiInfo.setName(parameter.getName());

        InfoAI infoAiForParam = parameter.getAnnotation(InfoAI.class);
        if (infoAiForParam != null) {
            argumentAiInfo.setDescription(infoAiForParam.description());
        }

        Class<?> typeToAnalyze = parameterType.isArray() ? parameterType.getComponentType() : parameterType;

        InfoAIClass annotation = typeToAnalyze.getAnnotation(InfoAIClass.class);
        if (annotation != null) {
            processTypeAnnotation(argumentAiInfo, typeToAnalyze, annotation);
            List<AiFieldInfo> fieldInfoList = fieldInfoExtractor.extractFieldsInfo(typeToAnalyze);
            argumentAiInfo.setFieldsInfo(fieldInfoList);
        }

        return argumentAiInfo;
    }


    private void processTypeAnnotation(ArgumentAiInfo argumentAiInfo, Class<?> type, InfoAIClass annotation) {
        argumentAiInfo.setDescription(annotation.description());
        argumentAiInfo.setCreationType(annotation.creationType());

        if (annotation.creationType() == CreationType.ENUM && type.isInterface()) {
            argumentAiInfo.setUseAsKeyInStorage(true);
            argumentAiInfo.setAvailableOptions(fieldInfoExtractor.getEnumOptions(type));
            argumentAiInfo.setAvailableOptions(fieldInfoExtractor.getEnumOptions(type));
        }
    }

}