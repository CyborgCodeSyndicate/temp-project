package com.theairebellion.zeus.ui.service.tables;

import com.theairebellion.zeus.ui.components.table.base.TableComponentType;

/**
 * Represents the default table types used within the framework.
 * <p>
 * This enum implements {@link TableComponentType} and provides a default implementation
 * for table components that do not require custom types.
 * </p>
 *
 * <p>
 * It is primarily used in conjunction with the {@link TableElement} interface
 * to standardize table interactions.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public enum DefaultTableTypes implements TableComponentType {

    /**
     * The default table type.
     */
    DEFAULT;

    /**
     * Retrieves the table type as an {@link Enum}.
     *
     * @return The table type as an {@link Enum}.
     */
    @Override
    public Enum<?> getType() {
        return this;
    }
}
