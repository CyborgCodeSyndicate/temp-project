package com.theairebellion.zeus.ai.metadata.extractor;

import com.theairebellion.zeus.ai.metadata.model.AiMethodInfo;
import com.theairebellion.zeus.ai.metadata.model.ArgumentAiInfo;
import com.theairebellion.zeus.ai.metadata.util.SignatureGenerator;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.annotations.InfoAI;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class MethodInfoExtractor {

    private final ArgumentInfoExtractor argumentInfoExtractor;
    private final UsageProvider usageProvider;


    public MethodInfoExtractor(ArgumentInfoExtractor argumentInfoExtractor, UsageProvider usageProvider) {
        this.argumentInfoExtractor = argumentInfoExtractor;
        this.usageProvider = usageProvider;
    }


    public List<AiMethodInfo> extractMethodsInfo(List<Method> methods, InfoAIClass infoAIClass) {
        return methods.stream()
                   .map(method -> createMethodInfo(method, infoAIClass))
                   .collect(Collectors.toList());
    }


    private AiMethodInfo createMethodInfo(Method method, InfoAIClass infoAIClass) {
        AiMethodInfo aiMethodInfo = new AiMethodInfo();
        aiMethodInfo.setName(method.getName());
        aiMethodInfo.setReturnClass(method.getReturnType());

        InfoAI infoAIMethod = method.getAnnotation(InfoAI.class);
        if (infoAIMethod != null && infoAIClass != null) {
            aiMethodInfo.setLevel(infoAIClass.level());
            aiMethodInfo.setDescription(infoAIMethod.description());

            String methodSignature = SignatureGenerator.generateMethodSignature(method);
            aiMethodInfo.setUsage(usageProvider.getUsageList(methodSignature));
        }

        List<ArgumentAiInfo> arguments = argumentInfoExtractor.extractArgumentsInfo(method);
        aiMethodInfo.setArgumentsInfo(arguments);

        return aiMethodInfo;
    }

}