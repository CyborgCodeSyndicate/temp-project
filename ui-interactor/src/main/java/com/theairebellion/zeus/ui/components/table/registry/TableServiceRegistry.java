package com.theairebellion.zeus.ui.components.table.registry;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing table-related services.
 * <p>
 * This class provides a centralized registry for storing and retrieving
 * {@link TableInsertion} and {@link TableFilter} services based on their
 * associated {@link ComponentType}. It ensures efficient lookups and
 * dynamic service assignment for table operations.
 * </p>
 *
 * <p>Used in {@code TableImpl} and {@code TableServiceImpl} to manage
 * cell insertion and filtering mechanisms.</p>
 *
 * @author Cyborg Code Syndicate
 */
public class TableServiceRegistry {

    /**
     * Stores registered {@link TableInsertion} services mapped to their component types.
     */
    private final Map<Class<? extends ComponentType>, TableInsertion> tableInsertionRegistry = new ConcurrentHashMap<>();

    /**
     * Stores registered {@link TableFilter} services mapped to their component types.
     */
    private final Map<Class<? extends ComponentType>, TableFilter> tableFilterRegistry = new ConcurrentHashMap<>();

    /**
     * Registers a {@link TableInsertion} service for a specific {@link ComponentType}.
     *
     * @param type    The component type for which the service is registered.
     * @param service The {@link TableInsertion} implementation to be registered.
     */
    public void registerService(Class<? extends ComponentType> type, TableInsertion service) {
        tableInsertionRegistry.put(type, service);
    }

    /**
     * Registers a {@link TableFilter} service for a specific {@link ComponentType}.
     *
     * @param type    The component type for which the service is registered.
     * @param service The {@link TableFilter} implementation to be registered.
     */
    public void registerService(Class<? extends ComponentType> type, TableFilter service) {
        tableFilterRegistry.put(type, service);
    }

    /**
     * Retrieves the registered {@link TableInsertion} service for the specified component type.
     *
     * @param type The component type associated with the service.
     * @return The corresponding {@link TableInsertion} service, or {@code null} if not registered.
     */
    public TableInsertion getTableService(Class<? extends ComponentType> type) {
        return tableInsertionRegistry.get(type);
    }

    /**
     * Retrieves the registered {@link TableFilter} service for the specified component type.
     *
     * @param type The component type associated with the service.
     * @return The corresponding {@link TableFilter} service, or {@code null} if not registered.
     */
    public TableFilter getFilterService(Class<? extends ComponentType> type) {
        return tableFilterRegistry.get(type);
    }

}
