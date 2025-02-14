package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.annotation.TestService;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.components.accordion.AccordionServiceImpl;
import com.theairebellion.zeus.ui.components.alert.AlertServiceImpl;
import com.theairebellion.zeus.ui.components.button.ButtonServiceImpl;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputService;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.components.link.LinkServiceImpl;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListServiceImpl;
import com.theairebellion.zeus.ui.components.loader.LoaderServiceImpl;
import com.theairebellion.zeus.ui.components.modal.ModalServiceImpl;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioServiceImpl;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxComponentType;
import com.theairebellion.zeus.ui.components.checkbox.CheckboxServiceImpl;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.select.SelectServiceImpl;
import com.theairebellion.zeus.ui.components.tab.TabServiceImpl;
import com.theairebellion.zeus.ui.components.table.filters.TableFilter;
import com.theairebellion.zeus.ui.components.table.insertion.TableInsertion;
import com.theairebellion.zeus.ui.components.table.registry.TableServiceRegistry;
import com.theairebellion.zeus.ui.components.table.service.TableServiceImpl;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.InsertionServiceElementImpl;
import com.theairebellion.zeus.ui.service.tables.TableServiceFluent;
import lombok.Getter;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

@TestService("UI")
@Getter
public class UIServiceFluent<T extends UIServiceFluent<?>> extends FluentService {

    private InputServiceFluent<T> inputField;
    private ButtonServiceFluent<T> buttonField;
    private RadioServiceFluent<T> radioField;
    private CheckboxServiceFluent<T> checkboxField;
    private SelectServiceFluent<T> selectField;
    private ListServiceFluent<T> listField;
    private LoaderServiceFluent<T> loaderField;
    private LinkServiceFluent<T> linkField;
    private AlertServiceFluent<T> alertField;
    private TabServiceFluent<T> tabField;
    private ModalServiceFluent<T> modalField;
    private AccordionServiceFluent<T> accordionField;
    private TableServiceFluent<T> table;
    private ValidationServiceFluent<T> validation;
    private NavigationServiceFluent<T> navigation;
    private SmartWebDriver driver;
    private InterceptorServiceFluent<T> interceptor;
    private InsertionServiceRegistry serviceRegistry;
    private TableServiceRegistry tableServiceRegistry;
    private InsertionServiceFluent<T> insertionService;


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
        buttonField = new ButtonServiceFluent(this, quest.getStorage(), new ButtonServiceImpl(driver), driver);
        radioField = new RadioServiceFluent(this, quest.getStorage(), new RadioServiceImpl(driver), driver);
        checkboxField = new CheckboxServiceFluent(this, quest.getStorage(), new CheckboxServiceImpl(driver), driver);
        selectField = new SelectServiceFluent(this, quest.getStorage(), new SelectServiceImpl(driver), driver);
        listField = new ListServiceFluent(this, quest.getStorage(), new ItemListServiceImpl(driver), driver);
        loaderField = new LoaderServiceFluent(this, quest.getStorage(), new LoaderServiceImpl(driver), driver);
        linkField = new LinkServiceFluent(this, quest.getStorage(), new LinkServiceImpl(driver), driver);
        alertField = new AlertServiceFluent(this, quest.getStorage(), new AlertServiceImpl(driver), driver);
        tabField = new TabServiceFluent(this, quest.getStorage(), new TabServiceImpl(driver), driver);
        modalField = new ModalServiceFluent(this, quest.getStorage(), new ModalServiceImpl(driver), driver);
        accordionField = new AccordionServiceFluent(this, quest.getStorage(), new AccordionServiceImpl(driver), driver);
        validation = new ValidationServiceFluent(this, driver);
        navigation = new NavigationServiceFluent(this, driver);
        interceptor = new InterceptorServiceFluent(this, quest.getStorage());
        InputService inputService = new InputServiceImpl(driver);
        inputField = new InputServiceFluent(this, quest.getStorage(), inputService, driver);
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
        serviceRegistry.registerService(RadioComponentType.class, radioField);
        serviceRegistry.registerService(CheckboxComponentType.class, checkboxField);
        serviceRegistry.registerService(SelectComponentType.class, selectField);
        serviceRegistry.registerService(ItemListComponentType.class, listField);
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
