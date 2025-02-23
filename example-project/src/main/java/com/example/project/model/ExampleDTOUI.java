package com.example.project.model;

import com.example.project.ui.elements.Bakery.ButtonFields;
import com.example.project.ui.elements.InputFields;
import com.theairebellion.zeus.ui.annotations.InsertionElement;
import com.theairebellion.zeus.ui.annotations.InsertionField;
import com.theairebellion.zeus.ui.components.base.ComponentType;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import com.theairebellion.zeus.ui.selenium.UIElement;
import lombok.Builder;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

@Data
@Builder
public class ExampleDTOUI {

    @InsertionField(locator = @FindBy(css = "locator"), type = InputComponentType.class,
        componentType = "MD_INPUT_TYPE",
        order = 1)
    private String textName;

    @InsertionElement(locatorClass = InputFields.class, elementEnum = InputFields.Data.USERNAME, order = 1)
    private String textNameOther;

    @InsertionElement(locatorClass = ButtonFields.class, elementEnum = ButtonFields.Data.SIGN_IN_BUTTON, order = 2)
    private String buttonElement;

}
