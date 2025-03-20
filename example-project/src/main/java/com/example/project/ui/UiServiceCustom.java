package com.example.project.ui;

import com.theairebellion.zeus.ai.metadata.model.classes.Level;
import com.theairebellion.zeus.annotations.InfoAI;
import com.theairebellion.zeus.annotations.InfoAIClass;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.AlertServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.ButtonServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.CheckboxServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.InputServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.InsertionServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.InterceptorServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.LinkServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.ListServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.NavigationServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.RadioServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.SelectServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.ValidationServiceFluent;
import com.theairebellion.zeus.ui.service.tables.TableServiceFluent;

@InfoAIClass(level = Level.MIDDLE,
    description = "UiServiceCustom can be called from Quest object and provides methods for accessing different UI services")
public class UiServiceCustom extends UIServiceFluent<UiServiceCustom> {


    public UiServiceCustom(SmartWebDriver driver, SuperQuest quest) {
        super(driver);
        this.quest = quest;
        postQuestSetupInitialization();
    }


    @InfoAI(description = "Retrieves the input service that has all functions for manipulation of inputs")
    public InputServiceFluent<UiServiceCustom> input() {
        return getInputField();
    }


    public TableServiceFluent<UiServiceCustom> table() {
        return getTable();
    }


    public InterceptorServiceFluent<UiServiceCustom> interceptor() {
        return getInterceptor();
    }


    public InsertionServiceFluent<UiServiceCustom> insertion() {
        return getInsertionService();
    }


    public ButtonServiceFluent<UiServiceCustom> button() {
        return getButtonField();
    }


    public RadioServiceFluent<UiServiceCustom> radio() {
        return getRadioField();
    }


    public SelectServiceFluent<UiServiceCustom> select() {
        return getSelectField();
    }


    public CheckboxServiceFluent<UiServiceCustom> checkbox() {
        return getCheckboxField();
    }


    public ListServiceFluent<UiServiceCustom> list() {
        return getListField();
    }


    public LinkServiceFluent<UiServiceCustom> link() {
        return getLinkField();
    }


    public AlertServiceFluent<UiServiceCustom> alert() {
        return getAlertField();
    }


    public NavigationServiceFluent<UiServiceCustom> browser() {
        return getNavigation();
    }


    public ValidationServiceFluent<UiServiceCustom> validate() {
        return getValidation();
    }

}
