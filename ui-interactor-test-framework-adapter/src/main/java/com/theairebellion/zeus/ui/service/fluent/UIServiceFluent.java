package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.service.TableServiceImpl;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.InsertionServiceElementImpl;
import com.theairebellion.zeus.ui.service.tables.TableServiceFluent;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

@TestService("UI")
public class UIServiceFluent<T extends UIServiceFluent<?>> extends FluentService {

    protected InputServiceFluent<T> inputField;
    protected TableServiceFluent<T> table;
    protected SmartWebDriver driver;
    protected InterceptorServiceFluent<T> interceptor;
    private InsertionServiceRegistry serviceRegistry;
    private TableServiceRegistry tableServiceRegistry;
    protected InsertionServiceFluent<T> insertionService;


    @Autowired
    public UIServiceFluent(SmartWebDriver driver) {
        this.driver = driver;
    }


    public T validate(Runnable assertion) {
        return (T) super.validate(assertion);
    }


    public T validate(Consumer<SoftAssertions> assertion) {
        return (T) super.validate(assertion);
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
        table = new TableServiceFluent(this, quest.getStorage(), new TableServiceImpl(driver, tableServiceRegistry),
                driver);
        insertionService = new InsertionServiceFluent(
                new InsertionServiceElementImpl(serviceRegistry, driver), this,
                quest.getStorage());
    }


    private void registerInsertionServices(InputService inputService) {
        serviceRegistry.registerService(InputComponentType.class, inputService);
    }


    private void registerTableServices(InputService inputService) {
        tableServiceRegistry.registerService(InputComponentType.class, (TableFilter) inputService);
        tableServiceRegistry.registerService(InputComponentType.class, (TableInsertion) inputService);

    }

    protected SmartWebDriver getDriver() {
        return driver;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected UIServiceFluent<T> clone() {
        UIServiceFluent<T> cloned = new UIServiceFluent<>(this.driver);

        cloned.quest = this.quest; // quest is 'protected' in FluentService

        cloned.inputField = this.inputField;
        cloned.table = this.table;
        cloned.interceptor = this.interceptor;
        cloned.serviceRegistry = this.serviceRegistry;
        cloned.tableServiceRegistry = this.tableServiceRegistry;
        cloned.insertionService = this.insertionService;

        return cloned;
    }


}
