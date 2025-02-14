package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.button.Button;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public interface Tab extends Button {

    boolean isSelected(SmartWebElement container, String buttonText);

    boolean isSelected(SmartWebElement container);

    boolean isSelected(String buttonText);

    boolean isSelected(By buttonLocator);
}
