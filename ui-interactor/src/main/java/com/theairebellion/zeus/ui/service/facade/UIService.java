package com.theairebellion.zeus.ui.service.facade;

import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListService;
import com.theairebellion.zeus.ui.components.list.ItemListServiceImpl;
import com.theairebellion.zeus.ui.components.loader.LoaderService;
import com.theairebellion.zeus.ui.components.loader.LoaderServiceImpl;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioService;
import com.theairebellion.zeus.ui.components.radio.RadioServiceImpl;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.select.SelectService;
import com.theairebellion.zeus.ui.components.select.SelectServiceImpl;
import com.theairebellion.zeus.ui.insertion.InsertionService;
import com.theairebellion.zeus.ui.insertion.InsertionServiceFieldImpl;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

@Getter
public class UIService {


    private final WebDriver driver;
    private InputService inputField;
    private RadioService radioField;
    private SelectService selectField;
    private ItemListService listField;
    private LoaderService loaderField;
    private final InsertionServiceRegistry serviceRegistry;
    private final InsertionService insertionService;


    public UIService(WebDriver driver) {
        SmartSelenium smartSelenium = new SmartSelenium(driver);
        this.driver = driver;
        inputField = new InputServiceImpl(smartSelenium);
        radioField = new RadioServiceImpl(smartSelenium);
        selectField = new SelectServiceImpl(smartSelenium);
        listField = new ItemListServiceImpl(smartSelenium);
        loaderField = new LoaderServiceImpl(smartSelenium);
        serviceRegistry = new InsertionServiceRegistry();
        registerInsertionServices();
        insertionService = new InsertionServiceFieldImpl(serviceRegistry);
    }


    private void registerInsertionServices() {
        serviceRegistry.registerService(InputComponentType.class, inputField);
        serviceRegistry.registerService(RadioComponentType.class, radioField);
        serviceRegistry.registerService(SelectComponentType.class, selectField);
        serviceRegistry.registerService(ItemListComponentType.class, listField);
    }

}
