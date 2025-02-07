package com.theairebellion.zeus.ui.service.tables;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.table.service.TableImpl;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

@ImplementationOfType("DEFAULT")
public class DefaultTable extends TableImpl {

    public DefaultTable(final SmartWebDriver smartWebDriver) {
        super(smartWebDriver);
    }

}
