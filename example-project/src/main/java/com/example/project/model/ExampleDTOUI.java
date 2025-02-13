package com.example.project.model;

import com.example.project.ui.elements.InputFields;
import com.theairebellion.zeus.ui.annotations.InsertionElement;
import com.theairebellion.zeus.ui.annotations.InsertionField;
import com.theairebellion.zeus.ui.components.input.InputComponentType;
import lombok.Builder;
import lombok.Data;
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


}
