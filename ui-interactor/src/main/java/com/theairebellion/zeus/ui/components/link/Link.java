package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.button.Button;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;

public interface Link extends Button {

    void doubleClick(SmartWebElement container, String buttonText);

    void doubleClick(SmartWebElement container);

    void doubleClick(String buttonText);

    void doubleClick(By buttonLocator);
}
