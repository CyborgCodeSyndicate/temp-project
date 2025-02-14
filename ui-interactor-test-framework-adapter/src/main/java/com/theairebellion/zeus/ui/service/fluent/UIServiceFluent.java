package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.service.TableServiceImpl;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.InsertionServiceElementImpl;
import com.theairebellion.zeus.ui.service.tables.TableServiceFluent;
import com.theairebellion.zeus.ui.validator.UiTableValidator;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@WorldName("UI")
@Service
@Scope("prototype")
@Lazy
public class UIServiceFluent extends FluentService {

    private InputServiceFluent inputField;
    private TableServiceFluent table;
    private SmartWebDriver driver;
    private InterceptorServiceFluent interceptor;
    private InsertionServiceRegistry serviceRegistry;
    private TableServiceRegistry tableServiceRegistry;
    private InsertionServiceFluent insertionService;

    @Autowired
    private UiTableValidator uiTableValidator;

    @Autowired
    public UIServiceFluent(SmartWebDriver driver) {
        this.driver = driver;
    }


    public UIServiceFluent validate(Runnable assertion) {
        return (UIServiceFluent) super.validate(assertion);
    }


    public UIServiceFluent validate(Consumer<SoftAssertions> assertion) {
        return (UIServiceFluent) super.validate(assertion);
    }


    @Override
    protected void postQuestSetupInitialization() {
        InputService inputService = new InputServiceImpl(driver);
        inputField = new InputServiceFluent(this, quest.getStorage(), inputService, driver);
        interceptor = new InterceptorServiceFluent(this, quest.getStorage());
        serviceRegistry = new InsertionServiceRegistry();
        registerInsertionServices(inputService);
        tableServiceRegistry = new TableServiceRegistry();
        registerTableServices(inputService);
        table = new TableServiceFluent(this, quest.getStorage(), new TableServiceImpl(driver, tableServiceRegistry, uiTableValidator),
            driver);
        insertionService = new InsertionServiceFluent(
            new InsertionServiceElementImpl(serviceRegistry, driver), this,
            quest.getStorage());
    }


    private SmartWebDriver getDriver() {
        return driver;
    }


    private void registerInsertionServices(InputService inputService) {
        serviceRegistry.registerService(InputComponentType.class, inputService);
    }


    private void registerTableServices(InputService inputService) {
        tableServiceRegistry.registerService(InputComponentType.class, (TableFilter) inputService);
        tableServiceRegistry.registerService(InputComponentType.class, (TableInsertion) inputService);

    }

}
