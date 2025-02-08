package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.framework.quest.Quest;
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
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.InsertionServiceElementImpl;
import manifold.ext.rt.api.Jailbreak;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTML;
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
    private ValidationServiceFluent validation;
    private SmartWebDriver driver;
    private InterceptorServiceFluent interceptor;
    private InsertionServiceRegistry serviceRegistry;
    private InsertionServiceFluent insertionService;


    @Autowired
    public UIServiceFluent(SmartWebDriver driver) {
        this.driver = driver;
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


    public UIServiceFluent validateTextInField(HTML.Tag tag, String text, boolean soft) {
        return validation.validateTextInField(tag, text, soft);
    }


    public UIServiceFluent validateTextInField(HTML.Tag tag, String text) {
        return validation.validateTextInField(tag, text, false);
    }


    public UIServiceFluent validateTextInField(String text) {
        HTML.Tag tag = HTML.Tag.I;
        return validation.validateTextInField(tag, text, false);
    }


    @Override
    protected void postQuestSetupInitialization() {
        inputField = new InputServiceFluent(this, quest.getStorage(), new InputServiceImpl(driver), driver);
        buttonField = new ButtonServiceFluent(this, quest.getStorage(), new ButtonServiceImpl(driver), driver);
        radioField = new RadioServiceFluent(this, quest.getStorage(), new RadioServiceImpl(driver), driver);
        selectField = new SelectServiceFluent(this, quest.getStorage(), new SelectServiceImpl(driver), driver);
        listField = new ItemListServiceFluent(this, quest.getStorage(), new ItemListServiceImpl(driver), driver);
        loaderField = new LoaderServiceFluent(this, quest.getStorage(), new LoaderServiceImpl(driver), driver);
        linkField = new LinkServiceFluent(this, quest.getStorage(), new LinkServiceImpl(driver), driver);
        alertField = new AlertServiceFluent(this, quest.getStorage(), new AlertServiceImpl(driver), driver);
        tabField = new TabServiceFluent(this, quest.getStorage(), new TabServiceImpl(driver), driver);
        validation = new ValidationServiceFluent(this, driver);
        interceptor = new InterceptorServiceFluent(this, quest.getStorage());
        serviceRegistry = new InsertionServiceRegistry();
        registerInsertionServices();
        insertionService = new InsertionServiceFluent(new InsertionServiceElementImpl(serviceRegistry, driver), this,
            quest.getStorage());
    }


    private SmartWebDriver getDriver() {
        return driver;
    }

    public UIServiceFluent navigate(String url) {
        getDriver().manage().window().maximize();
        getDriver().get(url);
        return this;
    }

    public UIServiceFluent back() {
        getDriver().navigate().back();
        return this;
    }
}
