package com.theairebellion.zeus.ui.components.tab;

import com.theairebellion.zeus.ui.components.button.Button;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public interface Tab extends Button {

    boolean isSelected(WebElement container, String buttonText);

    boolean isSelected(WebElement container);

    boolean isSelected(String buttonText);

    boolean isSelected(By buttonLocator);
}
