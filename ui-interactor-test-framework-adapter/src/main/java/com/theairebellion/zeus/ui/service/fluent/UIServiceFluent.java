package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.components.alert.AlertServiceImpl;
import com.theairebellion.zeus.ui.components.button.ButtonServiceImpl;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.components.link.LinkServiceImpl;
import com.theairebellion.zeus.ui.components.list.ItemListComponentType;
import com.theairebellion.zeus.ui.components.list.ItemListServiceImpl;
import com.theairebellion.zeus.ui.components.loader.LoaderServiceImpl;
import com.theairebellion.zeus.ui.components.radio.RadioComponentType;
import com.theairebellion.zeus.ui.components.radio.RadioServiceImpl;
import com.theairebellion.zeus.ui.components.select.SelectComponentType;
import com.theairebellion.zeus.ui.components.select.SelectServiceImpl;
import com.theairebellion.zeus.ui.components.tab.TabServiceImpl;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.SmartSelenium;
import com.theairebellion.zeus.ui.selenium.UIDriver;
import com.theairebellion.zeus.ui.service.InsertionServiceElementImpl;
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
    private ButtonServiceFluent buttonField;
    private RadioServiceFluent radioField;
    private SelectServiceFluent selectField;
    private ItemListServiceFluent listField;
    private LoaderServiceFluent loaderField;
    private LinkServiceFluent linkField;
    private AlertServiceFluent alertField;
    private TabServiceFluent tabField;
    private SmartSelenium smartSelenium;
    private UIDriver uiDriver;
    private InterceptorServiceFluent interceptor;
    private InsertionServiceRegistry serviceRegistry;
    private InsertionServiceFluent insertionService;


    @Autowired
    public UIServiceFluent(UIDriver uiDriver) {
        this.uiDriver = uiDriver;
        smartSelenium = new SmartSelenium(uiDriver.getDriver());
    }


    private void registerInsertionServices() {
        serviceRegistry.registerService(InputComponentType.class, inputField);
        serviceRegistry.registerService(RadioComponentType.class, radioField);
        serviceRegistry.registerService(SelectComponentType.class, selectField);
        serviceRegistry.registerService(ItemListComponentType.class, listField);
    }


    public UIServiceFluent validate(Runnable assertion) {
        return (UIServiceFluent) super.validate(assertion);
    }


    public UIServiceFluent validate(Consumer<SoftAssertions> assertion) {
        return (UIServiceFluent) super.validate(assertion);
    }


    @Override
    protected void postQuestSetupInitialization() {
        inputField = new InputServiceFluent(this, quest.getStorage(), new InputServiceImpl(smartSelenium));
        buttonField = new ButtonServiceFluent(this, quest.getStorage(), new ButtonServiceImpl(smartSelenium));
        radioField = new RadioServiceFluent(this, quest.getStorage(), new RadioServiceImpl(smartSelenium));
        selectField = new SelectServiceFluent(this, quest.getStorage(), new SelectServiceImpl(smartSelenium));
        listField = new ItemListServiceFluent(this, quest.getStorage(), new ItemListServiceImpl(smartSelenium));
        loaderField = new LoaderServiceFluent(this, quest.getStorage(), new LoaderServiceImpl(smartSelenium));
        linkField = new LinkServiceFluent(this, quest.getStorage(), new LinkServiceImpl(smartSelenium));
        alertField = new AlertServiceFluent(this, quest.getStorage(), new AlertServiceImpl(smartSelenium));
        tabField = new TabServiceFluent(this, quest.getStorage(), new TabServiceImpl(smartSelenium));
        interceptor = new InterceptorServiceFluent(this, quest.getStorage());
        serviceRegistry = new InsertionServiceRegistry();
        registerInsertionServices();
        insertionService = new InsertionServiceFluent(new InsertionServiceElementImpl(serviceRegistry), this,
                quest.getStorage());
    }

    public UIServiceFluent navigate(String url) {
        uiDriver.getDriver().manage().window().maximize();
        uiDriver.getDriver().get(url);
        return this;
    }

    public UIServiceFluent back() {
        uiDriver.getDriver().navigate().back();
        return this;
    }
}
