package com.theairebellion.zeus.ui.components.link;

import com.theairebellion.zeus.ui.components.button.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Link extends Button {

    void doubleClick(WebElement container, String buttonText);

    void doubleClick(WebElement container);

    void doubleClick(String buttonText);

    void doubleClick(By buttonLocator);
}
