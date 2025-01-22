package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.log.LogUI;

import java.util.HashMap;
import java.util.Map;

public class InsertionServiceRegistry {

    private final Map<Class<? extends ComponentType>, Insertion> registry = new HashMap<>();


    public void registerService(Class<? extends ComponentType> type, Insertion service) {
        registry.put(type, service);
        LogUI.debug("Registered Insertion service: [{}] for type: [{}]",
                service.getClass().getSimpleName(), type.getSimpleName());
    }


    public Insertion getService(Class<? extends ComponentType> type) {
        Insertion service = registry.get(type);
        if (service == null) {
            LogUI.warn("No Insertion service found for type: [{}].", type.getSimpleName());
        } else {
            LogUI.debug("Retrieved Insertion service: [{}] for type: [{}]",
                    service.getClass().getSimpleName(), type.getSimpleName());
        }
        return service;
    }
}
