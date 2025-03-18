package com.theairebellion.zeus.maven.plugins.navigator.controller;

import com.theairebellion.zeus.ai.metadata.model.AiUsage;
import com.theairebellion.zeus.maven.plugins.navigator.model.ServiceType;
import com.theairebellion.zeus.maven.plugins.navigator.usage.AIUsageService;

import java.util.Set;

public class AIUsageController {

    private final AIUsageService usageService;


    public AIUsageController(AIUsageService usageService) {
        this.usageService = usageService;
    }


    public AiUsage generateUsage(boolean useRest, boolean useUI, boolean useDB) {
        return usageService.generateUsage(useRest, useUI, useDB);
    }


    public AiUsage generateUsage(Set<ServiceType> serviceTypes) {
        return usageService.generateUsage(serviceTypes);
    }

}
