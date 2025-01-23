package com.theairebellion.zeus.ui.components.checkbox;

import com.theairebellion.zeus.ui.util.strategy.Strategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Checkbox {

    void select(WebElement container, String... checkBoxText);

    String select(WebElement container, Strategy strategy);

    void select(String... checkBoxText);

    void select(By... checkBoxLocator);


}
