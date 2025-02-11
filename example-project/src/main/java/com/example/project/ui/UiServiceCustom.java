package com.example.project.ui;

import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.InputServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.InsertionServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.InterceptorServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import com.theairebellion.zeus.ui.service.tables.TableServiceFluent;

public class UiServiceCustom extends UIServiceFluent<UiServiceCustom> {


    public UiServiceCustom(SmartWebDriver driver, SuperQuest quest) {
        super(driver);
        this.quest = quest;
        postQuestSetupInitialization();
    }

    public InputServiceFluent<UiServiceCustom> input() {
        return inputField;
    }


    public TableServiceFluent<UiServiceCustom> table() {
        return table;
    }


    public InterceptorServiceFluent<UiServiceCustom> interceptor() {
        return interceptor;
    }


    public InsertionServiceFluent<UiServiceCustom> insertion() {
        return insertionService;
    }

}
