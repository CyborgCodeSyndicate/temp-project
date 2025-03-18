package com.theairebellion.zeus.ui.service.tables;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.table.service.TableImpl;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

/**
 * Default implementation of a table component extending {@link TableImpl}.
 * <p>
 * This class is annotated with {@code @ImplementationOfType("DEFAULT")}, indicating that it serves as the
 * default implementation of a table in the framework. Currently, it does not add additional functionality
 * beyond what is provided by {@link TableImpl}.
 * </p>
 *
 * <p>
 * This class is a placeholder and can be extended in the future to provide additional table-specific behavior.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
@ImplementationOfType("DEFAULT")
public class DefaultTable extends TableImpl {

    /**
     * Constructs an instance of {@code DefaultTable} using the provided WebDriver.
     *
     * @param smartWebDriver The instance of {@link SmartWebDriver} used for interacting with the table.
     */
    public DefaultTable(final SmartWebDriver smartWebDriver) {
        super(smartWebDriver);
    }

}
