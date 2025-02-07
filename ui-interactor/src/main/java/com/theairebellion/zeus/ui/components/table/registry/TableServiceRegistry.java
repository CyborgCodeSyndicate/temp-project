package com.theairebellion.zeus.ui.components.table.registry;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableServiceRegistry {

    private final Map<Class<? extends ComponentType>, TableInsertion> tableInsertionRegistry = new ConcurrentHashMap<>();
    private final Map<Class<? extends ComponentType>, TableFilter> tableFilterRegistry = new ConcurrentHashMap<>();


    public void registerService(Class<? extends ComponentType> type, TableInsertion service) {
        tableInsertionRegistry.put(type, service);
    }


    public void registerService(Class<? extends ComponentType> type, TableFilter service) {
        tableFilterRegistry.put(type, service);
    }


    public TableInsertion getTableService(Class<? extends ComponentType> type) {
        return tableInsertionRegistry.get(type);
    }


    public TableFilter getFilterService(Class<? extends ComponentType> type) {
        return tableFilterRegistry.get(type);
    }


}
