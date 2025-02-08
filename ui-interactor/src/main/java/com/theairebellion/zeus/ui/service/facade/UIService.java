package com.theairebellion.zeus.ui.service.facade;

import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.service.TableService;
import com.theairebellion.zeus.ui.components.table.service.TableServiceImpl;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.insertion.InsertionService;
import com.theairebellion.zeus.ui.insertion.InsertionServiceFieldImpl;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import lombok.Getter;

@Getter
public class UIService {


    private final SmartWebDriver driver;
    private InputService inputField;
    private final InsertionServiceRegistry serviceRegistry;
    private final InsertionService insertionService;

    private final TableServiceRegistry tableServiceRegistry;
    private final TableService tableService;


    public UIService(SmartWebDriver driver) {
        this.driver = driver;
        inputField = new InputServiceImpl(driver);
        serviceRegistry = new InsertionServiceRegistry();
        tableServiceRegistry = new TableServiceRegistry();
        registerInsertionServices();
        insertionService = new InsertionServiceFieldImpl(serviceRegistry);
        tableService = new TableServiceImpl(driver, tableServiceRegistry);
    }


    private void registerInsertionServices() {
        serviceRegistry.registerService(InputComponentType.class, inputField);
        tableServiceRegistry.registerService(InputComponentType.class, (TableFilter) inputField);
        tableServiceRegistry.registerService(InputComponentType.class, (TableInsertion) inputField);
    }

}
