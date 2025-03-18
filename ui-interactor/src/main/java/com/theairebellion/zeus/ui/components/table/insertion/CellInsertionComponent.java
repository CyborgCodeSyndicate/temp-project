package com.theairebellion.zeus.ui.components.table.insertion;

import com.theairebellion.zeus.ui.components.base.ComponentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents a configuration for inserting values into a table cell.
 * <p>
 * This component defines the insertion mechanism using a specified
 * {@link ComponentType}, along with its order of execution.
 * </p>
 *
 * <p>It is primarily used in {@code CellLocator} and {@code TableImpl}.</p>
 *
 * @author Cyborg Code Syndicate
 */
@AllArgsConstructor
@Getter
public class CellInsertionComponent {

    /**
     * The type of component used for insertion.
     */
    private Class<? extends ComponentType> type;

    /**
     * The specific component type identifier.
     */
    private String componentType;

    /**
     * The execution order for insertion, determining priority when multiple insertions occur.
     */
    int order;
}
