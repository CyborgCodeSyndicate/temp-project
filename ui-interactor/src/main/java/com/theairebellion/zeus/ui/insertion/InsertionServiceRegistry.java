package com.theairebellion.zeus.ui.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InsertionServiceRegistry {


    private final Map<Class<? extends ComponentType>, Insertion> registry = new ConcurrentHashMap<>();


    public void registerService(Class<? extends ComponentType> type, Insertion service) {
        registry.put(type, service);
    }


    public Insertion getService(Class<? extends ComponentType> type) {
        return registry.get(type);
    }
}
