package com.theairebellion.zeus.ui.service.facade;

import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
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


    public UIService(SmartWebDriver driver) {
        this.driver = driver;
        inputField = new InputServiceImpl(driver);
        serviceRegistry = new InsertionServiceRegistry();
        registerInsertionServices();
        insertionService = new InsertionServiceFieldImpl(serviceRegistry);
    }


    private void registerInsertionServices() {
        serviceRegistry.registerService(InputComponentType.class, inputField);
    }

}
