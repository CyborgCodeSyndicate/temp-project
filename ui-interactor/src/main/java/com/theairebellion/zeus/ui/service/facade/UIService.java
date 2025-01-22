package com.theairebellion.zeus.ui.service.facade;

import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.insertion.InsertionService;
import com.theairebellion.zeus.ui.insertion.InsertionServiceFieldImpl;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

@Getter
public class UIService {


    private final WebDriver driver;
    private InputService inputField;
    private final InsertionServiceRegistry serviceRegistry;
    private final InsertionService insertionService;


    public UIService(WebDriver driver) {
        SmartSelenium smartSelenium = new SmartSelenium(driver);
        this.driver = driver;
        LogUI.info("UIService initialized with driver: [{}]", driver.getClass().getSimpleName());
        inputField = new InputServiceImpl(smartSelenium);
        serviceRegistry = new InsertionServiceRegistry();
        registerInsertionServices();
        insertionService = new InsertionServiceFieldImpl(serviceRegistry);
        LogUI.debug("UIService is fully set up. InputService: [{}], InsertionService: [{}]",
                inputField.getClass().getSimpleName(), insertionService.getClass().getSimpleName());
    }


    private void registerInsertionServices() {
        serviceRegistry.registerService(InputComponentType.class, inputField);
    }

}
