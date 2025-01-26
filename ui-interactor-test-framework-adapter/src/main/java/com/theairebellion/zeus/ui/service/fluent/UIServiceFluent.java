package com.theairebellion.zeus.ui.service.fluent;

import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.chain.FluentService;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.components.input.InputServiceImpl;
import com.theairebellion.zeus.ui.insertion.InsertionServiceRegistry;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
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
    }


    public UIServiceFluent validate(Runnable assertion) {
        return (UIServiceFluent) super.validate(assertion);
    }


    public UIServiceFluent validate(Consumer<SoftAssertions> assertion) {
        return (UIServiceFluent) super.validate(assertion);
    }


    @Override
    protected void postQuestSetupInitialization() {
        inputField = new InputServiceFluent(this, quest.getStorage(), new InputServiceImpl(driver));
        interceptor = new InterceptorServiceFluent(this, quest.getStorage());
        serviceRegistry = new InsertionServiceRegistry();
        registerInsertionServices();
        insertionService = new InsertionServiceFluent(new InsertionServiceElementImpl(serviceRegistry), this,
                quest.getStorage());
    }

}
