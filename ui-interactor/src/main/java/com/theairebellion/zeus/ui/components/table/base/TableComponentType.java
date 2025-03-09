package com.theairebellion.zeus.ui.components.table.base;

import com.theairebellion.zeus.ui.components.base.ComponentType;

/**
 * Represents a marker interface for table-related component types.
 * This interface extends {@link ComponentType} and is used to classify
 * various table component types within the framework.
 *
 * <p>Implementations of this interface define specific table behaviors,
 * such as filtering, sorting, and data insertion.</p>
 *
 * <p>Typically used in conjunction with {@code TableService} and
 * {@code TableElement} to manage different table types dynamically.</p>
 *
 * @author Cyborg Code Syndicate
 */
public interface TableComponentType extends ComponentType {
}
