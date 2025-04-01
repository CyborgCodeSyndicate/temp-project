package com.bakery.project.ui.tables;

import com.theairebellion.zeus.ui.annotations.ImplementationOfType;
import com.theairebellion.zeus.ui.components.table.service.TableImpl;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;

@ImplementationOfType("SIMPLE")
public class ExampleOfTable extends TableImpl {

    protected ExampleOfTable(final SmartWebDriver smartWebDriver) {
        super(smartWebDriver);
    }


}

